"""Content checks for the textbook sources.

Run on its own:  python scripts/check.py
scripts/build.py runs it too. Exit code 1 means at least one ERROR.
"""

from __future__ import annotations

import csv
import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from bookutil import MAX_LINE, ROOT, all_chapters, chapter_included, extract_listing, load_manifest  # noqa: E402

FORBIDDEN = [
    (r"\bTODO\b", "ERROR"),
    (r"\bTBD\b", "ERROR"),
    (r"\bFIXME\b", "ERROR"),
    (r"add examples here", "ERROR"),
    (r"explain(ed)? similarly", "ERROR"),
    (r"omitted for brevity", "ERROR"),
    (r"content omitted", "ERROR"),
    (r"lorem ipsum", "ERROR"),
    (r"\band so on\b", "WARN"),
    (r"left as an exercise", "WARN"),
    (r"\betc\.\)?$", "WARN"),
]

REQUIRED_IN_CHAPTER = [
    (r"^:::\s*\{?\s*\.objectives", "objectives box (::: objectives)"),
    (r"^:::\s*\{?\s*\.quiz", "warm-up quiz box (::: quiz)"),
    (r"^##\s+Summary\b", "'## Summary' section"),
    (r"^##\s+Exercises\b", "'## Exercises' section"),
    (r"^##\s+Solutions\b", "'## Solutions' section"),
]

FENCE = re.compile(r"^(`{3,}|~{3,})")
ATTR = re.compile(r'(\w[\w-]*)="([^"]*)"')
GENERIC_OUTSIDE_CODE = re.compile(r"\b[A-Z][A-Za-z]*<[\w?, \[\]<>]+>")


class Report:
    def __init__(self) -> None:
        self.items: list[tuple[str, str, str]] = []

    def add(self, level: str, where: str, msg: str) -> None:
        self.items.append((level, where, msg))

    @property
    def errors(self) -> int:
        return sum(1 for lvl, _, _ in self.items if lvl == "ERROR")

    @property
    def warnings(self) -> int:
        return sum(1 for lvl, _, _ in self.items if lvl == "WARN")


def prose_lines(text: str):
    """Yield (line_number, line) for lines outside fenced code blocks."""
    in_code = None
    for i, line in enumerate(text.splitlines(), 1):
        m = FENCE.match(line)
        if m:
            if in_code is None:
                in_code = m.group(1)[0] * len(m.group(1))
                continue
            if line.strip().startswith(in_code):
                in_code = None
                continue
        if in_code is None:
            yield i, line


def code_blocks(text: str):
    """Yield (line_number, info_string) for every fenced code block opening."""
    in_code = None
    for i, line in enumerate(text.splitlines(), 1):
        m = FENCE.match(line)
        if not m:
            continue
        if in_code is None:
            in_code = m.group(1)[0] * len(m.group(1))
            yield i, line[len(m.group(1)):].strip()
        elif line.strip().startswith(in_code):
            in_code = None


def check_markdown(path: Path, rep: Report, chapter: dict | None) -> dict:
    rel = path.relative_to(ROOT).as_posix()
    text = path.read_text(encoding="utf-8")
    words = 0
    for n, line in prose_lines(text):
        for pattern, level in FORBIDDEN:
            if re.search(pattern, line, re.IGNORECASE if level == "ERROR" else 0):
                rep.add(level, f"{rel}:{n}", f"forbidden phrase /{pattern}/")
        stripped = re.sub(r"`[^`]*`", "", line)
        if GENERIC_OUTSIDE_CODE.search(stripped):
            rep.add("WARN", f"{rel}:{n}", "generic type outside backticks")
        words += len(re.findall(r"[A-Za-z0-9']+", stripped))

    # Code blocks: includes and outputs must resolve.
    for n, info in code_blocks(text):
        attrs = dict(ATTR.findall(info))
        if "include" in attrs:
            src = ROOT / attrs["include"]
            if not src.is_file():
                rep.add("ERROR", f"{rel}:{n}", f"include not found: {attrs['include']}")
                continue
            try:
                lines = extract_listing(src.read_text(encoding="utf-8"), attrs.get("region"))
            except ValueError as exc:
                rep.add("ERROR", f"{rel}:{n}", f"{attrs['include']}: {exc}")
                continue
            if not lines:
                rep.add("ERROR", f"{rel}:{n}", f"empty listing from {attrs['include']}")
        if ".output" in info or ".input" in info:
            frm = attrs.get("from")
            if not frm:
                rep.add("ERROR", f"{rel}:{n}", "output/input block without from=")
            elif not (ROOT / (frm + ".java")).is_file():
                rep.add("ERROR", f"{rel}:{n}", f"no source for from={frm}")
            elif ".input" in info and not (ROOT / (frm + ".in")).is_file():
                rep.add("ERROR", f"{rel}:{n}", f"input file missing: {frm}.in")
        if info.startswith("java") and "include" not in attrs:
            rep.add("ERROR", f"{rel}:{n}", "bare java block: use include= or .fragment")

    info = {"words": words, "exercises": [], "solutions": []}
    if chapter is None:
        return info

    for pattern, what in REQUIRED_IN_CHAPTER:
        if not re.search(pattern, text, re.MULTILINE):
            rep.add("ERROR", rel, f"missing {what}")

    num = str(chapter["number"])
    for n, line in prose_lines(text):
        if line.startswith(":::") and (".exercise" in line or ".solution" in line):
            attrs = dict(ATTR.findall(line))
            number = attrs.get("number", "")
            kind = "exercises" if ".exercise" in line else "solutions"
            if not number.startswith(num + "."):
                rep.add("ERROR", f"{rel}:{n}", f"{kind[:-1]} number '{number}' must start with {num}.")
            info[kind].append(number)
            if kind == "exercises" and attrs.get("level") not in ("Easy", "Medium", "Hard", "Challenge"):
                rep.add("ERROR", f"{rel}:{n}", f"exercise {number}: level must be Easy/Medium/Hard/Challenge")
    ex, so = info["exercises"], info["solutions"]
    for dup in sorted({x for x in ex if ex.count(x) > 1}):
        rep.add("ERROR", rel, f"duplicate exercise number {dup}")
    for missing in sorted(set(ex) - set(so)):
        rep.add("ERROR", rel, f"exercise {missing} has no solution")
    for extra in sorted(set(so) - set(ex)):
        rep.add("ERROR", rel, f"solution {extra} has no exercise")
    if len(ex) < 4:
        rep.add("WARN", rel, f"only {len(ex)} exercises")
    return info


def check_java(rep: Report, referenced: set[str]) -> int:
    count = 0
    for base in ("code-examples", "solutions"):
        for path in sorted((ROOT / base).rglob("*.java")):
            count += 1
            rel = path.relative_to(ROOT).as_posix()
            text = path.read_text(encoding="utf-8")
            for n, line in enumerate(text.splitlines(), 1):
                if len(line.replace("\t", "    ")) > MAX_LINE:
                    rep.add("ERROR", f"{rel}:{n}", f"line longer than {MAX_LINE} characters")
                if re.search(r"\b(TODO|FIXME|TBD)\b", line):
                    rep.add("ERROR", f"{rel}:{n}", "TODO/FIXME/TBD in code")
                if "\t" in line:
                    rep.add("WARN", f"{rel}:{n}", "tab character (use spaces)")
            stem = rel[:-5]
            if stem not in referenced and not path.stem.endswith("Check"):
                rep.add("WARN", rel, "not shown in the book (no include= or from= refers to it)")
    return count


def referenced_sources(paths: list[Path]) -> set[str]:
    refs: set[str] = set()
    for p in paths:
        text = p.read_text(encoding="utf-8")
        for _, info in code_blocks(text):
            attrs = dict(ATTR.findall(info))
            if "include" in attrs and attrs["include"].endswith(".java"):
                refs.add(attrs["include"][:-5])
            if "from" in attrs:
                refs.add(attrs["from"])
    return refs


def check_problems_csv(rep: Report) -> dict:
    path = ROOT / "references" / "problems.csv"
    stats = {"total": 0, "verified": 0, "unverified": 0}
    if not path.is_file():
        return stats
    with open(path, encoding="utf-8", newline="") as f:
        rows = list(csv.DictReader(f))
    expected = {"id", "chapter", "title", "platform", "url", "difficulty", "pattern",
                "essential", "status", "date_checked"}
    if rows and set(rows[0].keys()) != expected:
        rep.add("ERROR", "references/problems.csv", f"columns must be {sorted(expected)}")
    for r in rows:
        stats["total"] += 1
        st = (r.get("status") or "").strip()
        if st not in ("verified", "unverified"):
            rep.add("ERROR", "references/problems.csv", f"{r.get('id')}: bad status '{st}'")
        else:
            stats[st] += 1
        if not (r.get("url") or "").startswith("https://"):
            rep.add("ERROR", "references/problems.csv", f"{r.get('id')}: url must be https")
    return stats


def run_checks() -> tuple[Report, dict]:
    rep = Report()
    manifest = load_manifest()
    md_files: list[Path] = []
    chapter_info: dict[str, dict] = {}
    for entry in manifest.get("front", []) + manifest.get("back", []):
        p = ROOT / entry["file"]
        if p.is_file():
            md_files.append(p)
            check_markdown(p, rep, None)
    for _, ch in all_chapters(manifest):
        if chapter_included(ch):
            p = ROOT / ch["file"]
            md_files.append(p)
            chapter_info[ch["file"]] = check_markdown(p, rep, ch)
    java_count = check_java(rep, referenced_sources(md_files))
    problems = check_problems_csv(rep)
    return rep, {"chapters": chapter_info, "java_files": java_count, "problems": problems}


def main() -> int:
    rep, stats = run_checks()
    for level, where, msg in rep.items:
        print(f"{level:5} {where}: {msg}")
    for f, info in stats["chapters"].items():
        print(f"INFO  {f}: {info['words']} prose words, {len(info['exercises'])} exercises")
    print(f"check: {rep.errors} error(s), {rep.warnings} warning(s)")
    return 1 if rep.errors else 0


if __name__ == "__main__":
    raise SystemExit(main())
