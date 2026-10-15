"""Generate exercises/chNN-*.md and solutions/chNN-*.md from the chapter sources.

The chapter Markdown files are the single source of truth; these generated files make
the exercises and solutions easy to read on their own (e.g. on GitHub). Code listings
are expanded from the Java files so the generated files are self-contained.
"""

from __future__ import annotations

import re
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from bookutil import BUILD, ROOT, all_chapters, chapter_included, extract_listing, write_text  # noqa: E402

ATTR = re.compile(r'(\w[\w-]*)="([^"]*)"')
DIV_OPEN = re.compile(r"^(:{3,})\s*\{(.*)\}\s*$|^(:{3,})\s*([\w-]+)\s*$")
DIV_CLOSE = re.compile(r"^:{3,}\s*$")
FENCE = re.compile(r"^(`{3,}|~{3,})(.*)$")
HEADING_ID = re.compile(r"^#{1,6}\s+(.*?)\s*\{[^}]*#([\w-]+)[^}]*\}\s*$")

KIND_LABEL = {
    "definition": "Definition", "note": "Note", "warning": "Warning", "tip": "Interview tip",
    "example": "Example", "optional": "Optional", "keyidea": "Key idea",
    "objectives": "In this chapter", "quiz": "Warm-up quiz",
}


def heading_titles(text: str) -> dict[str, str]:
    out = {}
    for line in text.splitlines():
        m = HEADING_ID.match(line)
        if m:
            out[m.group(2)] = m.group(1)
    return out


def inline(line: str, titles: dict[str, str]) -> str:
    line = re.sub(r"\[([^\]]*)\]\{\.idx[^}]*\}", r"\1", line)

    def ref(m):
        target = m.group(1)
        title = titles.get(target)
        return f"the section “{title}”" if title else "the referenced section"
    line = re.sub(r'\[\]\{\.ref to="([^"]+)"\}', ref, line)
    return line


def expand_code(info: str, body: list[str]) -> list[str]:
    attrs = dict(ATTR.findall(info))
    if "include" in attrs:
        src = ROOT / attrs["include"]
        code = extract_listing(src.read_text(encoding="utf-8"), attrs.get("region"))
        lang = "java" if attrs["include"].endswith(".java") else ""
        return [f"```{lang}", f"// {attrs['include']}", *code, "```"]
    if ".output" in info and "from" in attrs:
        path = BUILD / "outputs" / (attrs["from"] + ".txt")
        text = path.read_text(encoding="utf-8").rstrip() if path.is_file() else "(run the build)"
        return ["```text", *text.splitlines(), "```"]
    if ".input" in info and "from" in attrs:
        path = ROOT / (attrs["from"] + ".in")
        return ["```text", *path.read_text(encoding="utf-8").rstrip().splitlines(), "```"]
    lang = "java" if ".fragment" in info else "text"
    return [f"```{lang}", *body, "```"]


def collect(text: str, kind: str) -> list[list[str]]:
    """Return the line blocks of every ::: {.<kind> ...} div (with nested content)."""
    lines = text.splitlines()
    blocks, i = [], 0
    while i < len(lines):
        line = lines[i]
        if line.startswith(":::") and f".{kind}" in line and "{" in line:
            depth, j, body = 1, i + 1, [line]
            in_code = None
            while j < len(lines) and depth > 0:
                ln = lines[j]
                fm = FENCE.match(ln)
                if fm and in_code is None:
                    in_code = fm.group(1)
                elif in_code and ln.strip().startswith(in_code) and ln.strip() == in_code[0] * len(ln.strip()):
                    in_code = None
                elif in_code is None:
                    if DIV_CLOSE.match(ln):
                        depth -= 1
                    elif DIV_OPEN.match(ln):
                        depth += 1
                body.append(ln)
                j += 1
            blocks.append(body)
            i = j
        else:
            i += 1
    return blocks


def to_markdown(block: list[str], kind: str, titles: dict[str, str]) -> list[str]:
    attrs = dict(ATTR.findall(block[0]))
    if kind == "exercise":
        head = f"### Exercise {attrs.get('number', '?')}"
        if attrs.get("level"):
            head += f" ({attrs['level']})"
    else:
        head = f"### Solution {attrs.get('number', '?')}"
    if attrs.get("title"):
        head += f": {attrs['title']}"
    out = [head, ""]
    body = block[1:-1]
    i = 0
    while i < len(body):
        line = body[i]
        fm = FENCE.match(line)
        if fm:
            fence, info = fm.group(1), fm.group(2)
            j, code = i + 1, []
            while j < len(body) and body[j].strip() != fence:
                code.append(body[j])
                j += 1
            out += expand_code(info, code)
            i = j + 1
            continue
        m = DIV_OPEN.match(line)
        if m:
            info = m.group(2) or m.group(4) or ""
            k = next((c for c in KIND_LABEL if f".{c}" in info or info == c), None)
            a = dict(ATTR.findall(info))
            label = KIND_LABEL.get(k, "Note")
            out.append(f"> **{label}{': ' + a['title'] if a.get('title') else ''}**")
            i += 1
            continue
        if DIV_CLOSE.match(line):
            i += 1
            continue
        out.append(inline(line, titles))
        i += 1
    out.append("")
    return out


def generate(manifest: dict) -> None:
    for _, ch in all_chapters(manifest):
        if not chapter_included(ch):
            continue
        src = ROOT / ch["file"]
        text = src.read_text(encoding="utf-8")
        titles = heading_titles(text)
        stem = Path(ch["file"]).stem
        for kind, folder, word in (("exercise", "exercises", "Exercises"),
                                   ("solution", "solutions", "Solutions")):
            blocks = collect(text, kind)
            lines = [f"# Chapter {ch['number']}: {ch['title']} ({word})", "",
                     f"<!-- Generated by scripts/extract.py from {ch['file']}. "
                     "Edit the chapter, not this file. -->", ""]
            if kind == "solution":
                lines += ["Try each exercise in `exercises/` first. Solution code lives in "
                          f"`solutions/ch{ch['number']:02d}/`.", ""]
            for b in blocks:
                lines += to_markdown(b, kind, titles)
            write_text(ROOT / folder / f"{stem}.md", "\n".join(lines))


if __name__ == "__main__":
    from bookutil import load_manifest
    generate(load_manifest())
