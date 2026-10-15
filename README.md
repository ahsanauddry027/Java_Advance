# Advanced Java, DSA and Job Interview Preparation (2026)

A self-contained textbook for a computer science student who already knows Java
fundamentals and wants to use Java for data structures and algorithms (DSA), online
assessments, technical interviews, and backend development, with a focus on the
Bangladesh job market in 2026.

The main deliverable is the PDF **`Advanced_Java_DSA_Job_Preparation_2026.pdf`**, built
from the sources in this folder. The book is being written chapter by chapter; see
`PROGRESS.md` for what is finished.

## Folder layout

| Path | What it holds |
|------|---------------|
| `content/` | the book's text: one Markdown file per chapter, plus `front/` and `back/` matter and `book.json` (order, titles, page budgets, status) |
| `code-examples/chNN/` | every program shown in chapter NN; `ChNNCheck.java` holds that chapter's self-checks |
| `solutions/chNN/` | solution programs for chapter NN's exercises |
| `exercises/`, `solutions/*.md` | exercises and worked solutions per chapter, **generated** from `content/` by the build |
| `templates/` | the Typst page design (`book.typ`) and the Pandoc filter (`book.lua`) |
| `scripts/` | `build.py` (full build), `check.py` (content rules), `extract.py`, `listing.py` |
| `references.md` | sources, with the date each link was checked (also printed at the end of the PDF) |
| `references/problems.csv` | practice-problem links with verification status |
| `research/job-descriptions/` | job postings you collect for Chapter 28 (see the README inside) |
| `validation-report.md` | written by every build: what was compiled, run and checked |
| `CLAUDE.md`, `SYLLABUS.md`, `PROGRESS.md` | project rules, scope, and progress tracking |

## Rebuilding the PDF

You need:

- **Python 3.10+**
- **Pandoc 3.x**: `winget install --id JohnMacFarlane.Pandoc --exact`
- **Typst 0.13+**: `winget install --id Typst.Typst --exact`
- **A JDK 21 or newer.** The build compiles with `--release 21`, so any newer JDK works.
  The build finds `JAVA_HOME`, a `javac` on `PATH`, a Temurin/Microsoft/Oracle JDK in
  `Program Files`, or Android Studio's bundled JDK.

Then, from this folder:

```
python scripts/build.py
```

The build compiles and runs every Java program (each must pass its self-checks), runs the
content checks, regenerates `exercises/` and `solutions/*.md`, converts the chapters to
Typst, builds the PDF, and writes `validation-report.md`. Useful options:

```
python scripts/build.py --png 1-10     # also render pages 1-10 to build/png/ to inspect them
python scripts/build.py --skip-java    # reuse the previous Java outputs (faster)
python scripts/check.py                # content checks only
python scripts/listing.py code-examples/ch10/BalancedParentheses.java   # numbered listing
```

If a tool is installed somewhere unusual, set `PANDOC`, `TYPST` or `JAVA_HOME`.

## Using the materials to study

1. Read the chapter in the PDF, with the matching folder in `code-examples/` open in your
   editor. Run the programs and change them.
2. Do the exercises from the PDF or from `exercises/`. Write your own code first.
3. Compare with the worked solution in the PDF or in `solutions/`.
4. Follow the study plan in `study_roadmap.md` (written after the main chapters).

## Writing conventions (for contributors)

Chapter sources use Pandoc Markdown with a few conventions handled by `templates/book.lua`:

````
::: {.definition title="Stack"}        boxes: definition, note, warning, tip, example,
...                                     optional, keyidea, objectives, quiz
:::

```{.java include="code-examples/ch10/Foo.java"}          whole program
```
```{.java include="code-examples/ch10/Foo.java" region="push"}   one region
```
```{.output from="code-examples/ch10/Foo"}                what the program printed
```

::: {.exercise number="10.1" level="Easy" title="..."}
:::
::: {.solution number="10.1"}
:::

[]{.ref to="section-id"}     cross-reference      [term]{.idx}     index entry
## Heading {.optional}       optional material
````

A bare ```` ```java ```` block is rejected: every Java listing must come from a file the
build compiled.
