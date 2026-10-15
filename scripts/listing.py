"""Print a listing exactly as the book shows it, with line numbers.

    python scripts/listing.py code-examples/ch10/BalancedParentheses.java
    python scripts/listing.py code-examples/ch10/MonotonicStack.java nextGreater

Use it when writing line-by-line explanations, so line numbers in the prose match
the numbers printed in the PDF.
"""

import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))
from bookutil import ROOT, extract_listing  # noqa: E402


def main() -> int:
    if len(sys.argv) < 2:
        print(__doc__)
        return 2
    path = ROOT / sys.argv[1]
    region = sys.argv[2] if len(sys.argv) > 2 else None
    lines = extract_listing(path.read_text(encoding="utf-8"), region)
    for i, line in enumerate(lines, 1):
        print(f"{i:3}  {line}")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
