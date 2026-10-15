"""Merge new terms into content/back/glossary.md and keep it sorted.

    python scripts/glossary.py merge new-terms.md

`new-terms.md` uses the same Pandoc definition-list format as the glossary:

    Term
    :   Definition text, which may span
        several lines.

A term that already exists is replaced by the new definition.
"""

from __future__ import annotations

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from bookutil import ROOT, write_text  # noqa: E402

GLOSSARY = ROOT / "content" / "back" / "glossary.md"


def parse(text: str) -> dict[str, str]:
    entries: dict[str, str] = {}
    for block in text.strip().split("\n\n"):
        if "\n:   " not in block:
            continue
        term, _, desc = block.partition("\n:   ")
        entries[term.strip()] = " ".join(line.strip() for line in desc.split("\n"))
    return entries


def wrap(text: str, width: int = 88, indent: str = "    ") -> str:
    lines: list[str] = []
    current = ""
    for word in text.split():
        if current and len(current) + len(word) + 1 > width:
            lines.append(current)
            current = word
        else:
            current = f"{current} {word}" if current else word
    lines.append(current)
    return lines[0] + "".join("\n" + indent + line for line in lines[1:])


def merge(new_file: Path) -> int:
    text = GLOSSARY.read_text(encoding="utf-8")
    head, sep, _ = text.partition("\n\n")
    intro_end = text.index("\n\n", len(head) + 2)  # title, blank line, intro paragraph
    header = text[:intro_end].rstrip()
    entries = parse(text[intro_end:])
    entries.update(parse(new_file.read_text(encoding="utf-8")))
    out = header + "\n\n"
    for term in sorted(entries, key=str.lower):
        out += term + "\n:   " + wrap(entries[term]) + "\n\n"
    write_text(GLOSSARY, out.rstrip() + "\n")
    return len(entries)


def main() -> int:
    if len(sys.argv) != 3 or sys.argv[1] != "merge":
        print(__doc__)
        return 2
    print(f"glossary now has {merge(Path(sys.argv[2]))} entries")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
