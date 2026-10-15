Read CLAUDE.md and SYLLABUS.md fully before doing anything. Together they define the reader, the
teaching rules, the scope, and the quality gates for this project: a complete Advanced Java,
DSA and job-interview textbook (A4 PDF) plus runnable code, exercises, solutions, a study
roadmap, references and a validation report.

This session covers Phase 0 and Phase 1 only. Stop at the end of Phase 1 so I can review.

## Phase 0: Setup

1. Inspect the environment (JDK, Python, Pandoc, Typst, LaTeX, git) and report what you
   find. If something in the preferred toolchain is missing, give me the exact install
   command and wait for my OK. Do not install anything yourself.
2. Run `git init` if needed and create this structure:
   - `content/`: one Markdown file per chapter, plus front and back matter
   - `code-examples/chNN/`, `exercises/`, `solutions/chNN/`
   - `assets/` (diagrams, fonts), `templates/` (Typst template, Lua filters)
   - `scripts/` (`build`, `check`, compile-and-run helpers)
   - `research/job-descriptions/`, `references/problems.csv`, `references.md`
   - `build/` (output, git-ignored), `README.md`, `PROGRESS.md`
3. Write `PROGRESS.md`: a checklist of every chapter and every topic from SYLLABUS.md, each
   with a status (todo / in progress / done) and its page budget, plus a "Next up" line and
   the back-matter and final-validation tasks.
4. Build the toolchain: the Typst template (A4, cover, clickable TOC, numbered parts,
   chapters and sections, running headers, page numbers, callout boxes, Java syntax
   highlighting, readable code font, captioned figures and tables), the Lua filters
   (callouts; code includes from files, with optional `// region` markers), and
   `scripts/build` and `scripts/check` as described in CLAUDE.md.
5. Produce a 6–8 page style sample: cover, TOC, and one section that uses every element
   (each callout type, a complete-program listing, a method listing pulled from a region,
   a trace table, an ASCII diagram, a complexity table, an exercise and its solution).
   Render the pages to PNG, look at them, and fix problems.

## Phase 1: Pilot chapter

6. Write Chapter 10 (Stacks, queues and deques) completely, meeting the definition of done
   in CLAUDE.md. It is the pilot because it uses every format element: diagrams, dry-run
   tables, monotonic patterns, complexity analysis, interview Q&A, exercises and solutions.
7. Build the PDF, render at least 8 pages of Chapter 10 to PNG, inspect them, fix problems,
   and rebuild.
8. Report: the PDF's path, size and page count; Chapter 10's page count vs its budget; what
   was compiled and tested (quote the real command output); anything unverified; and any
   questions you have for me about style, depth or length.

Then stop. Once I approve the pilot (any style changes I ask for go into the "Style
decisions" section of CLAUDE.md), we will write the remaining chapters in book order,
one or two per session, using CONTINUE_PROMPT.md.
