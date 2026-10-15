"""Shared helpers for the build scripts: paths, tool discovery, manifest, listings."""

from __future__ import annotations

import glob
import json
import os
import re
import shutil
import subprocess
from dataclasses import dataclass
from pathlib import Path

ROOT = Path(__file__).resolve().parent.parent
BUILD = ROOT / "build"
MAX_LINE = 90


# --------------------------------------------------------------------------- tools

@dataclass
class Tools:
    pandoc: str
    typst: str
    javac: str
    java: str


def _first_file(candidates):
    for c in candidates:
        if c and Path(c).is_file():
            return str(c)
    return None


def _java_home_candidates():
    env = os.environ.get("JAVA_HOME")
    if env:
        yield Path(env)
    javac = shutil.which("javac")
    if javac:
        yield Path(javac).resolve().parent.parent
    pf = [os.environ.get("ProgramFiles", r"C:\Program Files")]
    for base in pf:
        for pattern in (r"Eclipse Adoptium\jdk-21*", r"Microsoft\jdk-21*", r"Java\jdk-21*",
                        r"Zulu\zulu-21*", r"Eclipse Adoptium\jdk-2*", r"Java\jdk-2*"):
            for hit in sorted(glob.glob(os.path.join(base, pattern)), reverse=True):
                yield Path(hit)
        yield Path(base) / "Android" / "Android Studio" / "jbr"


def find_tools() -> Tools:
    """Locate pandoc, typst, javac and java. Environment variables PANDOC, TYPST and
    JAVA_HOME override the search."""
    local = os.environ.get("LOCALAPPDATA", "")
    pandoc = os.environ.get("PANDOC") or shutil.which("pandoc") or _first_file([
        Path(local) / "Pandoc" / "pandoc.exe",
        r"C:\Program Files\Pandoc\pandoc.exe",
    ])
    typst = os.environ.get("TYPST") or shutil.which("typst") or _first_file(
        [Path(local) / "Microsoft" / "WinGet" / "Links" / "typst.exe"]
        + sorted(glob.glob(os.path.join(
            local, "Microsoft", "WinGet", "Packages", "Typst.Typst_*", "*", "typst.exe")))
    )
    javac = java = None
    for home in _java_home_candidates():
        exe = ".exe" if os.name == "nt" else ""
        jc, jv = home / "bin" / f"javac{exe}", home / "bin" / f"java{exe}"
        if jc.is_file() and jv.is_file():
            javac, java = str(jc), str(jv)
            break
    missing = [n for n, v in (("pandoc", pandoc), ("typst", typst), ("javac", javac)) if not v]
    if missing:
        raise SystemExit("Missing tools: " + ", ".join(missing)
                         + ". See README.md, section 'Rebuilding the PDF'.")
    return Tools(pandoc=pandoc, typst=typst, javac=javac, java=java)


def tool_versions(tools: Tools) -> dict:
    def first_line(cmd):
        try:
            r = subprocess.run(cmd, capture_output=True, text=True, timeout=30)
            return (r.stdout or r.stderr).strip().splitlines()[0]
        except Exception as exc:  # noqa: BLE001 - report whatever went wrong
            return f"unavailable ({exc})"
    return {
        "pandoc": first_line([tools.pandoc, "--version"]),
        "typst": first_line([tools.typst, "--version"]),
        "javac": first_line([tools.javac, "--version"]),
        "java": first_line([tools.java, "--version"]),
    }


# --------------------------------------------------------------------------- manifest

def load_manifest() -> dict:
    with open(ROOT / "content" / "book.json", encoding="utf-8") as f:
        return json.load(f)


def all_chapters(manifest: dict):
    for part in manifest["parts"]:
        for ch in part["chapters"]:
            yield part, ch


def chapter_included(ch: dict) -> bool:
    return ch.get("status", "todo") != "todo" and (ROOT / ch["file"]).is_file()


# --------------------------------------------------------------------------- listings

_REGION_OPEN = re.compile(r"^\s*//\s*region\s+([\w-]+)\s*$")
_REGION_CLOSE = re.compile(r"^\s*//\s*endregion\s+([\w-]+)\s*$")
_ANY_MARKER = re.compile(r"^\s*//\s*(region|endregion)(\s|$)")


def extract_listing(text: str, region: str | None = None) -> list[str]:
    """Same algorithm as templates/book.lua `extract`; keep the two in sync."""
    lines = text.replace("\r\n", "\n").split("\n")
    out: list[str] = []
    if region:
        inside = found = False
        for line in lines:
            m_open, m_close = _REGION_OPEN.match(line), _REGION_CLOSE.match(line)
            if m_open:
                if m_open.group(1) == region:
                    inside = found = True
            elif m_close:
                if m_close.group(1) == region:
                    inside = False
            elif _ANY_MARKER.match(line):
                raise ValueError("endregion must name its region")
            elif inside:
                out.append(line)
        if not found:
            raise ValueError(f"region '{region}' not found")
    else:
        out = [line for line in lines if not _ANY_MARKER.match(line)]
    out = [line.replace("\t", "    ").rstrip() for line in out]
    while out and out[0] == "":
        out.pop(0)
    while out and out[-1] == "":
        out.pop()
    indents = [len(line) - len(line.lstrip(" ")) for line in out if line]
    indent = min(indents) if indents else 0
    if indent:
        out = [line[indent:] for line in out]
    return out


def write_text(path: Path, text: str) -> None:
    """Write UTF-8 with LF line endings on every platform."""
    path.parent.mkdir(parents=True, exist_ok=True)
    with open(path, "w", encoding="utf-8", newline="\n") as f:
        f.write(text)
