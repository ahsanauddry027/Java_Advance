"""Build the textbook.

    python scripts/build.py                 # full build
    python scripts/build.py --png 1-8       # also render pages 1-8 to build/png/
    python scripts/build.py --skip-java     # reuse the last Java outputs

Steps:
  1. Compile every Java file under code-examples/ and solutions/ with --release 21
     (-Xlint:all -Werror) and run each program with a main method. Programs exit
     non-zero when a self-check fails. Captured output goes to build/outputs/ and is
     what the book shows.
  2. Run scripts/check.py (content rules).
  3. Regenerate exercises/ and solutions/ Markdown from the chapter sources.
  4. Convert every included Markdown file to Typst with Pandoc + templates/book.lua.
  5. Generate build/main.typ and compile the PDF with Typst.
  6. Write validation-report.md (also copied to build/).
"""

from __future__ import annotations

import argparse
import datetime as dt
import json
import re
import shutil
import subprocess
import sys
import time
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
import check  # noqa: E402
import extract  # noqa: E402
from bookutil import (  # noqa: E402
    BUILD,
    ROOT,
    all_chapters,
    chapter_included,
    find_tools,
    load_manifest,
    tool_versions,
    write_text,
)

# A classic main method, or a Java 25 instance main method in a compact source file.
MAIN_RE = re.compile(r"public\s+static\s+void\s+main\s*\(|^\s*void\s+main\s*\(", re.MULTILINE)
DIRECTIVE_RE = re.compile(r"//\s*build:\s*(.+)$", re.MULTILINE)


# --------------------------------------------------------------------------- java

def parse_directives(source: str) -> dict:
    """`// build: expect-exit=1`, `// build: no-run`, `// build: release=25`."""
    out = {}
    for m in DIRECTIVE_RE.finditer(source):
        for item in m.group(1).split():
            key, _, value = item.partition("=")
            out[key] = value or True
    return out


def compile_dir(tools, src_dir: Path, out_dir: Path, release: str) -> dict:
    sources = sorted(src_dir.glob("*.java"))
    shutil.rmtree(out_dir, ignore_errors=True)
    out_dir.mkdir(parents=True)
    cmd = [tools.javac, "--release", release, "-encoding", "UTF-8",
           "-Xlint:all,-serial,-this-escape", "-Werror", "-d", str(out_dir)]
    cmd += [str(s) for s in sources]
    t0 = time.time()
    r = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")
    return {
        "dir": src_dir.relative_to(ROOT).as_posix(),
        "files": len(sources),
        "release": release,
        "ok": r.returncode == 0,
        "seconds": round(time.time() - t0, 1),
        "log": (r.stdout + r.stderr).strip(),
    }


def run_program(tools, src: Path, classes: Path, directives: dict) -> dict:
    rel = src.relative_to(ROOT).as_posix()[:-5]
    out_txt = BUILD / "outputs" / (rel + ".txt")
    err_txt = BUILD / "outputs" / (rel + ".err.txt")
    stdin_file = src.with_suffix(".in")
    stdin = stdin_file.read_text(encoding="utf-8") if stdin_file.is_file() else ""
    expected = int(directives.get("expect-exit", 0))
    cmd = [tools.java, "-ea", "-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8",
           "-cp", str(classes), src.stem]
    t0 = time.time()
    try:
        r = subprocess.run(cmd, input=stdin, capture_output=True, text=True,
                           encoding="utf-8", errors="replace", timeout=120)
        code, out, err = r.returncode, r.stdout, r.stderr
    except subprocess.TimeoutExpired:
        code, out, err = -1, "", "TIMEOUT after 120 s"
    write_text(out_txt, out.replace("\r\n", "\n"))
    write_text(err_txt, err.replace("\r\n", "\n"))
    return {
        "program": rel,
        "ok": code == expected,
        "exit": code,
        "expected": expected,
        "seconds": round(time.time() - t0, 2),
        "stderr": err.strip()[-2000:],
    }


def java_step(tools) -> dict:
    compiles, runs = [], []
    for base in ("code-examples", "solutions"):
        for d in sorted(p for p in (ROOT / base).glob("ch*") if p.is_dir()):
            groups = [(d, "21")]
            if (d / "java25").is_dir():
                groups.append((d / "java25", "25"))
            for src_dir, release in groups:
                if not any(src_dir.glob("*.java")):
                    continue
                classes = BUILD / "classes" / src_dir.relative_to(ROOT)
                c = compile_dir(tools, src_dir, classes, release)
                compiles.append(c)
                status = "ok" if c["ok"] else "FAILED"
                print(f"  javac --release {release} {c['dir']}: {c['files']} file(s) {status}")
                if not c["ok"]:
                    print(c["log"])
                    continue
                for src in sorted(src_dir.glob("*.java")):
                    text = src.read_text(encoding="utf-8")
                    directives = parse_directives(text)
                    if not MAIN_RE.search(text) or directives.get("no-run"):
                        continue
                    r = run_program(tools, src, classes, directives)
                    runs.append(r)
                    if not r["ok"]:
                        print(f"  RUN FAILED {r['program']} (exit {r['exit']}, "
                              f"expected {r['expected']})\n{r['stderr']}")
    print(f"  java: {sum(c['ok'] for c in compiles)}/{len(compiles)} directories compiled, "
          f"{sum(r['ok'] for r in runs)}/{len(runs)} programs passed")
    return {"compiles": compiles, "runs": runs}


# --------------------------------------------------------------------------- typst

def tstr(s: str) -> str:
    return '"' + s.replace("\\", "\\\\").replace('"', '\\"') + '"'


def pandoc_to_typst(tools, src: Path, out: Path, prefix: str, chapter: int | None) -> None:
    cmd = [tools.pandoc, str(src), "-f", "markdown-citations-raw_html-implicit_figures",
           "-t", "typst", "--wrap=preserve", "-L", str(ROOT / "templates" / "book.lua"),
           "-M", f"idprefix={prefix}", "-M", f"root={ROOT.as_posix()}"]
    if chapter is not None:
        cmd += ["-M", f"chapter={chapter}"]
    r = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")
    if r.returncode != 0:
        raise SystemExit(f"pandoc failed for {src.relative_to(ROOT)}:\n{r.stderr}")
    header = '#import "../../templates/book.typ": *\n\n'
    write_text(out, header + r.stdout)


def generate_main(manifest: dict, typ_files: dict, version: str, date: str) -> Path:
    lines = [
        '#import "../templates/book.typ": *',
        f'#show: book.with(title: {tstr(manifest["title"])})',
        "#cover(",
        f'  title: {tstr(manifest["title"])},',
        f'  subtitle: {tstr(manifest["subtitle"])},',
        f'  edition: {tstr(manifest["edition"])},',
        f'  version: {tstr(version)},',
        f'  date: {tstr(date)},',
        "  topics: (" + ", ".join(tstr(t) for t in manifest.get("topics", [])) + ",),",
        ")",
        '#set page(numbering: "i")',
        "#counter(page).update(1)",
        "#toc()",
        "#set heading(numbering: none)",
    ]
    for entry in manifest.get("front", []):
        if entry["file"] in typ_files:
            lines.append(f'#include "{typ_files[entry["file"]]}"')
    lines += ['#set page(numbering: "1")', "#counter(page).update(1)",
              "#set heading(numbering: heading-numbering)"]
    for part in manifest["parts"]:
        chapters = [ch for ch in part["chapters"] if chapter_included(ch)]
        if not chapters:
            continue
        chap_list = ", ".join(
            f"(number: {ch['number']}, title: {tstr(ch['title'])})" for ch in chapters)
        lines.append(
            f"#part-page(number: {part['number']}, title: {tstr(part['title'])}, "
            f"blurb: {tstr(part.get('blurb', ''))}, chapters: ({chap_list},))")
        for ch in chapters:
            lines.append(f'#include "{typ_files[ch["file"]]}"')
    lines.append("#pagebreak(weak: true)")
    lines.append("#boundary()")
    lines.append("#set heading(numbering: none)")
    lines.append("#set par(justify: false)")
    for entry in manifest.get("back", []):
        if entry["file"] in typ_files:
            lines.append(f'#include "{typ_files[entry["file"]]}"')
    lines += ["#index-page()", "#book-end()", ""]
    main = BUILD / "main.typ"
    write_text(main, "\n".join(lines))
    return main


def typst_step(tools, manifest: dict, version: str, date: str) -> dict:
    typ_dir = BUILD / "typ"
    shutil.rmtree(typ_dir, ignore_errors=True)
    typ_files: dict[str, str] = {}

    def convert(rel: str, prefix: str, chapter: int | None):
        src = ROOT / rel
        name = Path(rel).stem
        out = typ_dir / f"{name}.typ"
        pandoc_to_typst(tools, src, out, prefix, chapter)
        typ_files[rel] = f"typ/{name}.typ"

    for entry in manifest.get("front", []):
        if (ROOT / entry["file"]).is_file():
            convert(entry["file"], "front-", None)
    for _, ch in all_chapters(manifest):
        if chapter_included(ch):
            convert(ch["file"], f"ch{ch['number']:02d}-", ch["number"])
    for entry in manifest.get("back", []):
        if (ROOT / entry["file"]).is_file():
            convert(entry["file"], "back-", None)

    main = generate_main(manifest, typ_files, version, date)
    pdf = BUILD / manifest["output"]
    cmd = [tools.typst, "compile", "--root", str(ROOT), "--font-path",
           str(ROOT / "assets" / "fonts"), str(main), str(pdf)]
    t0 = time.time()
    r = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")
    seconds = round(time.time() - t0, 1)
    if r.returncode != 0:
        raise SystemExit(f"typst compile failed:\n{r.stderr}")
    warnings = r.stderr.strip()

    def query(selector: str):
        q = subprocess.run([tools.typst, "query", "--root", str(ROOT), "--font-path",
                            str(ROOT / "assets" / "fonts"), str(main), selector,
                            "--field", "value"],
                           capture_output=True, text=True, encoding="utf-8", errors="replace")
        if q.returncode != 0:
            raise SystemExit(f"typst query {selector} failed:\n{q.stderr}")
        return json.loads(q.stdout)

    starts = query("<chapter-start>")
    end_page = query("<book-end>")[0]
    stops = sorted([s["page"] for s in starts] + query("<boundary>") + [end_page + 1])
    pages = {}
    for s in starts:
        nxt = min(p for p in stops if p > s["page"])
        pages[s["number"]] = nxt - s["page"]
    final = ROOT / manifest["output"]
    shutil.copyfile(pdf, final)
    return {"pdf": final, "size": final.stat().st_size, "pages": end_page,
            "chapter_pages": pages, "seconds": seconds, "warnings": warnings}


def render_png(tools, pages: str) -> list[Path]:
    out_dir = BUILD / "png"
    shutil.rmtree(out_dir, ignore_errors=True)
    out_dir.mkdir(parents=True)
    cmd = [tools.typst, "compile", "--root", str(ROOT), "--font-path",
           str(ROOT / "assets" / "fonts"), str(BUILD / "main.typ"),
           str(out_dir / "page-{0p}.png"), "--format", "png", "--pages", pages, "--ppi", "110"]
    r = subprocess.run(cmd, capture_output=True, text=True, encoding="utf-8", errors="replace")
    if r.returncode != 0:
        raise SystemExit(f"png render failed:\n{r.stderr}")
    return sorted(out_dir.glob("*.png"))


# --------------------------------------------------------------------------- report

def write_report(manifest, versions, java, rep, stats, pdf, date) -> None:
    L = [f"# Validation report", "",
         f"Generated by `scripts/build.py` on {date}. Everything below comes from the "
         f"commands the build actually ran.", "", "## Tools", ""]
    L += [f"- {k}: `{v}`" for k, v in versions.items()]
    L += ["", "## Java", ""]
    if java is None:
        L.append("Skipped in this build (`--skip-java`); outputs from the previous run were used.")
    else:
        L += ["| Directory | Files | --release | Compiled |", "|---|---|---|---|"]
        for c in java["compiles"]:
            L.append(f"| `{c['dir']}` | {c['files']} | {c['release']} | "
                     f"{'yes' if c['ok'] else '**NO**'} |")
        ok = sum(r["ok"] for r in java["runs"])
        L += ["", f"Programs run: {len(java['runs'])}; passed (expected exit code, "
                  f"self-checks passed): {ok}.", ""]
        failed = [r for r in java["runs"] if not r["ok"]]
        for r in failed:
            L.append(f"- **FAILED** `{r['program']}`: exit {r['exit']} (expected {r['expected']})")
    L += ["", "## Content checks", "",
          f"{rep.errors} error(s), {rep.warnings} warning(s).", ""]
    for level, where, msg in rep.items:
        L.append(f"- {level} `{where}`: {msg}")
    L += ["", "## PDF", ""]
    if pdf:
        L += [f"- File: `{pdf['pdf'].name}`",
              f"- Size: {pdf['size'] / 1024 / 1024:.2f} MB ({pdf['size']:,} bytes)",
              f"- Pages: {pdf['pages']}",
              f"- Typst compile time: {pdf['seconds']} s", ""]
        L += ["| Chapter | Pages | Budget | Prose words | Exercises |", "|---|---|---|---|---|"]
        for _, ch in all_chapters(manifest):
            if not chapter_included(ch):
                continue
            got = pdf["chapter_pages"].get(ch["number"], "?")
            lo, hi = ch["budget"]
            info = stats["chapters"].get(ch["file"], {})
            flag = ""
            if isinstance(got, int) and not (lo * 0.8 <= got <= hi * 1.2):
                flag = " (outside budget ±20%)"
            L.append(f"| {ch['number']}. {ch['title']} | {got}{flag} | {lo}–{hi} | "
                     f"{info.get('words', '?')} | {len(info.get('exercises', []))} |")
        if pdf["warnings"]:
            L += ["", "Typst warnings:", "", "```", pdf["warnings"][:4000], "```"]
    p = stats["problems"]
    L += ["", "## Practice-problem links", "",
          f"{p['total']} listed in `references/problems.csv`: {p['verified']} verified, "
          f"{p['unverified']} unverified.", "",
          "## Not verified by this build", "",
          "- Visual layout is checked by rendering pages to PNG and looking at them; the "
          "build cannot do that by itself. See PROGRESS.md for which chapters were inspected.",
          "- Factual accuracy of prose is reviewed by hand against the sources in "
          "references.md; no automated check exists for it.", ""]
    text = "\n".join(L)
    write_text(ROOT / "validation-report.md", text)
    write_text(BUILD / "validation-report.md", text)


# --------------------------------------------------------------------------- main

def main() -> int:
    for stream in (sys.stdout, sys.stderr):
        stream.reconfigure(encoding="utf-8", errors="replace")
    ap = argparse.ArgumentParser(description=__doc__, formatter_class=argparse.RawTextHelpFormatter)
    ap.add_argument("--skip-java", action="store_true", help="reuse previous Java outputs")
    ap.add_argument("--png", metavar="PAGES", help="also render these pages, e.g. 1-8,20")
    args = ap.parse_args()

    tools = find_tools()
    versions = tool_versions(tools)
    manifest = load_manifest()
    now = dt.datetime.now()
    date = now.strftime("%Y-%m-%d %H:%M")
    version = manifest["version"]
    BUILD.mkdir(exist_ok=True)

    java = None
    if not args.skip_java:
        print("[1/5] Java: compile and run")
        java = java_step(tools)
    print("[2/5] Content checks")
    rep, stats = check.run_checks()
    for level, where, msg in rep.items:
        print(f"  {level:5} {where}: {msg}")
    print(f"  {rep.errors} error(s), {rep.warnings} warning(s)")
    print("[3/5] Exercises and solutions files")
    extract.generate(manifest)
    print("[4/5] Typst PDF")
    pdf = typst_step(tools, manifest, version, now.strftime("%d %B %Y"))
    print(f"  {pdf['pdf']} ({pdf['size'] / 1024 / 1024:.2f} MB, {pdf['pages']} pages)")
    if pdf["warnings"]:
        print("  typst warnings:\n" + pdf["warnings"][:3000])
    if args.png:
        files = render_png(tools, args.png)
        print(f"  rendered {len(files)} page(s) to {BUILD / 'png'}")
    print("[5/5] Validation report")
    write_report(manifest, versions, java, rep, stats, pdf, date)

    java_failed = java is not None and (
        not all(c["ok"] for c in java["compiles"]) or not all(r["ok"] for r in java["runs"]))
    if java_failed or rep.errors:
        print("BUILD FINISHED WITH ERRORS (see validation-report.md)")
        return 1
    print("BUILD OK")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
