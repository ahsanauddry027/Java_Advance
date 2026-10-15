# CLAUDE.md — Advanced Java, DSA & Job Interview Textbook (2026)

Claude Code loads this file automatically in every session in this folder. It holds the
rules that never change. **Scope** lives in `SYLLABUS.md`. **Progress** lives in `PROGRESS.md`.

## Goal

Build a self-contained, professionally typeset A4 PDF textbook,
*Advanced Java, DSA and Job Interview Preparation (2026)*, plus runnable code, exercises,
solutions, a study roadmap, references and a validation report. It is my primary learning
resource, so I must be able to learn from it without any other material.

## Reader (me)

- CSE student in Bangladesh preparing for software engineering / developer roles in 2026
  (product companies, IT service firms, startups, multinationals hiring locally).
- Already knows Java fundamentals: types, operators, control flow, methods, arrays, strings,
  classes, constructors, the four OOP pillars, interfaces, basic exceptions.
  **Do not reteach these.** Revisit them only for pitfalls, internals and DSA usage.
- Goals: solve LeetCode / online-assessment problems independently in Java, analyze
  complexity, debug my own code, explain my decisions aloud in interviews, and know enough
  backend Java to be employable.
- Wants deep understanding, not memorized code. Plain, simple English. Define every
  technical term the first time it appears.
- Study time: about 2–3 hours/day, 5–6 days/week.
- Optional details (fill in; they make the roadmap realistic):
  - Year of study: ___
  - DSA problems solved so far (approx.): ___
  - When I start applying for jobs: ___
  - Preferred role (backend / general SWE / other): ___
  - Companies I'm targeting: ___

## Java baseline

- **Java 21 LTS is the baseline.** All main code compiles with `javac --release 21`.
- Java 25 LTS features: mention where relevant, label them clearly as "Java 25", keep them
  in separate files, and never use them in online-assessment templates.
- Online judges may run older Java versions. Every OA template states its minimum Java
  version at the top and avoids newer features.
- Always distinguish: language feature vs JVM behavior vs standard-library API vs
  implementation detail (e.g., OpenJDK/HotSpot specifics). A spec guarantee is not the same
  as how the current implementation happens to behave.

## Teaching structure (three tiers)

Every topic in `SYLLABUS.md` is tagged **[Core]**, **[Std]** or **[Opt]**.

**[Core]: full treatment, in this order:**

1. What: simple definition.
2. Why: the problem it solves.
3. How: step by step.
4. Internal working: JVM, memory, algorithm or library behavior.
5. Syntax breakdown: every important keyword, symbol, type parameter, lambda, annotation.
6. Complete code example.
7. Line-by-line explanation in execution order.
8. Dry run: sample input, intermediate states (trace tables / ASCII diagrams), output.
9. Time and space complexity, with justification.
10. Common mistakes and edge cases.
11. When to use it: DSA, interview and real-world scenarios.
12. Interview perspective: likely questions and model answers.
13. Practice exercises of increasing difficulty.
14. Solutions, placed in the chapter's Solutions section at the end, never next to the exercise.

For algorithms: show the brute force first when it teaches something, then *derive* the
optimized solution. Show how a developer would discover it, test it, and argue that it is
correct.

**[Std]: compact treatment.** What/Why/How, complete code + explanation, dry run if
nontrivial, complexity, pitfalls, 1–3 interview questions, 2–4 exercises with solutions.

**[Opt]: reference treatment.** Concise explanation, one example, when it is worth
learning. Visually labelled "Optional" in the PDF.

Every chapter **opens** with learning objectives, prerequisites (as section references)
and a 5-question warm-up quiz. Every chapter **closes** with a summary, a key-takeaways
table, exercises, solutions, and "what's next".

## One home chapter per topic

Each topic is taught fully in exactly one chapter: its *home*, as listed in `SYLLABUS.md`.
Everywhere else, give a one-line reminder and a cross-reference (e.g., "see §7.4") instead
of re-teaching. Chapters 21, 26, 30 and 31 are synthesis chapters: mostly cross-references,
worked selection examples, and tables.

## Code rules

- **Single source of truth.** Every code listing in the book is pulled in from a file
  under `code-examples/` or `solutions/` by the build, never pasted by hand. Method-only
  listings come from a marked region (`// region name` … `// endregion`) inside a compiled
  file. What the PDF shows is exactly what was compiled.
- Every listing is labelled **Complete program**, **Method** or **Fragment**. Fragments are
  never presented as compilable. Pseudocode is labelled as pseudocode.
- Complete programs have imports, one public class, and a `main` that runs the example on
  sample input and prints output. The book shows the expected output.
- Self-checks live in a separate `ChNNCheck.java` per chapter folder (and
  `ChNNSolutionsCheck.java` in `solutions/chNN/`), so the listings shown in the book stay
  clean. Checks compare against expected values or a brute-force version on many small
  random inputs, and throw `AssertionError` (non-zero exit) on failure.
- Maximum code line length: 90 characters (prevents clipped code in the PDF).
- Explicit types in introductory examples. Use `var` only after Chapter 23 introduces it.
  No raw types, no `Object`-based workarounds where generics fit, no deprecated APIs
  (except when showing why to avoid them, e.g., legacy `Stack`).
- Comparators: never subtraction (`(a, b) -> a - b`) for general `int` values. Use
  `Integer.compare`, `Comparator.comparingInt`, `Comparator.reverseOrder()`, and explain why
  subtraction can overflow. `PriorityQueue` is a min-heap by default; show a safe max-heap.
- Discuss Java-specific traps where relevant: `int` overflow, `Integer` caching and `==`,
  mutable keys in hash collections, reference aliasing, recursion depth and
  `StackOverflowError`, boxing cost.
- **JDK on this machine:** `javac` is not on PATH. Android Studio bundles a JDK 25 at
  `C:\Program Files\Android\Android Studio\jbr\bin\` that works with `--release 21`.
  If a separate JDK 21 (e.g., Temurin) is installed later, prefer it.
- **Never install software or download dependencies without asking me first.**

## Writing rules

- Simple English, short paragraphs, concrete examples. No filler, motivational padding or
  repeated boilerplate.
- Use tables, ASCII diagrams, memory layouts, recursion trees and trace tables wherever
  they help. Memory diagrams are conceptual models; say so.
- Put all code in backticks, including identifiers, generics (`List<Integer>`) and
  annotations (`@Override`).
- Callout blocks, rendered as distinct boxes: **Definition, Note, Warning, Interview Tip,
  Example, Optional**.
- Forbidden in finished content: `TODO`, `TBD`, "add examples here", "explain similarly",
  "omitted for brevity", or "and so on" used in place of real content. `scripts/check`
  must flag these.
- Practice problems: summarize statements in your own words; never copy problem text.
  Record every problem link in `references/problems.csv` with a status column
  (`verified` / `unverified`) and the date checked.

## Accuracy rules

- Never invent statistics, salaries, company interview processes, benchmarks, citations
  or URLs.
- Bangladesh hiring content: separate **Verified observation** (with source and date
  accessed) from **General guidance**. If evidence is missing, say so plainly. Prefer the
  job descriptions I save in `research/job-descriptions/` over browsing job sites.
- Cite official documentation (Java SE API, JLS, JEPs, OpenJDK, Spring, JUnit, Maven,
  Gradle) for nontrivial technical claims. Keep every source in `references.md`.
- Never claim that something was compiled, tested, verified or visually inspected unless it
  actually was. The validation report must reflect real command output.

## Build and quality gates

**Preferred toolchain (ask before installing anything):** Markdown chapter sources (Pandoc
flavour, fenced divs for callouts) → Pandoc with Lua filters (callouts, code includes) →
Typst → PDF. Typst can also export pages as PNG for visual inspection. If you believe
another toolchain is clearly better, propose it before switching. Content stays in Markdown
so the toolchain can change without rewriting chapters.

`scripts/build` (one command) must:

1. Compile all Java examples with `--release 21` and run their self-checks.
2. Run `scripts/check`: forbidden phrases, code line length, every chapter has
   Objectives / Exercises / Solutions sections, every code include resolves, word count per
   chapter compared with its budget in `SYLLABUS.md`.
3. Build the PDF (only chapters marked done or in progress in `PROGRESS.md`).
4. Write `build/validation-report.md` from the real results.

**A chapter is done only when:**

- every topic listed for it in `SYLLABUS.md` is covered at its tier;
- all of its code compiles and its self-checks pass;
- `scripts/check` is clean for it;
- the PDF builds;
- you have rendered at least 5 of its pages to PNG (including code-heavy and table-heavy
  pages), looked at them, and fixed layout problems;
- `PROGRESS.md` is updated;
- the work is committed to git.

## Session discipline

- Read `PROGRESS.md` first. Work on what it lists as next. Do not rewrite finished chapters
  unless I ask or the check script finds a problem in them.
- Finish chapters fully rather than starting many partially. If context runs low, stop at a
  clean point, record exactly where you stopped in `PROGRESS.md`, and commit.
- Commit after each completed chapter (local git only; never push unless I ask).
- Record any style decisions I approve in a "Style decisions" section at the end of this
  file so later sessions follow them.

## Style decisions

Recorded after the Chapter 10 pilot (2026-10-15). The user chose to continue without a
review stop, so these are the working conventions; change them here if the user asks.

**Toolchain (installed with the user's approval):** Pandoc 3.12.1 and Typst 0.15.1 via
winget; Java is compiled with Android Studio's bundled JDK 25.0.3 using `--release 21`.
`scripts/build.py` finds all three automatically. Build with `python scripts/build.py`;
add `--png 1-10` to render pages for inspection, `--skip-java` to reuse Java outputs.

**Markdown conventions** (implemented in `templates/book.lua`; see README.md):

- Program output is never typed by hand: use ```` ```{.output from="code-examples/chNN/Name"} ````,
  which shows what the build captured. Program input comes from `Name.in`.
- Use `region=` to show one method; mark regions in Java with `// region name` and
  `// endregion name`. Use `kind="Fields and methods"` when a region holds several members.
- Run `python scripts/listing.py <file> [region]` before writing line-by-line
  explanations, so line numbers match the PDF exactly.
- A paragraph that is only bold text (`**Syntax breakdown.**`) becomes a small run-in
  heading automatically; use this for "Syntax breakdown", "Line by line", "Dry run" when
  they stand alone, or write the label at the start of a paragraph.
- Pipe-table column widths follow the number of dashes in the separator row; make the
  dash counts proportional to the content.
- Table captions (`: caption`) are Markdown: put anything with `[]`, `{}` or `<>` in
  backticks.
- Powers of ten in prose: `10^5^` (superscript). No `$` math unless really needed.
- Cross-references inside a chapter: `[]{.ref to="heading-id"}`; give important headings
  explicit ids. References to other chapters: plain text ("Chapter 7"), because chapters
  are built incrementally and a missing label breaks the build.
- Mark index terms where a term is defined: `[term]{.idx}` or
  `[LIFO]{.idx term="LIFO (last in, first out)"}`. Add every defined term to
  `content/back/glossary.md`.
- Warm-up quiz answers go first in the Solutions section, under `### Warm-up quiz answers`.
- Exercise levels: Easy, Medium, Hard, Challenge. Aim for 12–15 exercises in a [Core]
  chapter, every one with a worked solution; coding solutions are compiled programs in
  `solutions/chNN/` with output shown.
- Avoid "and so on" and "etc." (the check script warns); list the items instead.

**Length:** the pilot came out at 39 pages for a 25–35 page budget (inside the ±20%
tolerance) with about 10,000 words of prose and 15 exercises. Treat that as the upper end
of the density to aim for.
