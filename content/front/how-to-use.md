# How to Use This Book

## How the book is organized

The book has seven parts. Part III is the heart of the book; the other parts prepare you
for it or help you use it in interviews and at work.

| Part | Chapters | What it gives you |
|------|----------|-------------------|
| I. Java for Problem Solving | 1–4 | Types and their traps, fast I/O, lambdas for comparators, advanced OOP, debugging and testing |
| II. Java Internals, Complexity and Collections | 5–7 | The JVM and memory, Big-O analysis, how every major collection works inside |
| III. Data Structures and Algorithms | 8–21 | Arrays to dynamic programming, each implemented, traced and analyzed |
| IV. Modern Java Features Worth Knowing | 22–23 | Streams in depth, records, pattern matching, concurrency basics |
| V. Reliable Solutions in Online Assessments | 24–25 | Templates, verdicts, and a step-by-step workflow |
| VI. Job Preparation in Bangladesh, 2026 | 26–28 | Model interview answers, backend Java with Spring Boot, a job-search plan |
| VII. Practice, Revision and Assessment | 29–31 | Practice problems, a question bank, a revision handbook |

: The seven parts of the book

Read Parts I and II before Part III unless you are already confident with Java collections
and Big-O notation. Inside Part III, the chapters are ordered so that each one only depends
on earlier ones. Every chapter lists its prerequisites at the start.

## How each topic is taught

Important topics get the full treatment, in this order: **what** the idea is, **why** it
exists, **how** it works step by step, how it works **internally**, the **syntax** in
detail, a complete **program**, a **line-by-line** explanation, a **dry run**, the
**complexity**, the **common mistakes**, **when to use** it, the **interview** angle, and
finally **exercises** with separate **solutions**.

Smaller topics use a shorter version of the same pattern. Material that is useful but not
needed for most interviews is marked with an Optional badge, like the heading of the last
section in this chapter. You can skip optional material on a first reading.

Every chapter starts with a box that lists its goals and prerequisites, followed by a short
warm-up quiz, and ends with a summary, exercises and solutions.

## Reading the boxes

Six kinds of boxes appear in the text. Each has its own color.

::: {.definition title="Algorithm"}
A precise, finite sequence of steps that solves a problem. Definition boxes give the exact
meaning of a term the first time it appears. The glossary at the end of the book collects
them.
:::

::: {.keyidea title="Invariants"}
A key-idea box states the one insight that makes a technique work. If you remember only
one thing from a section, remember its key idea.
:::

::: {.note}
A note adds detail that is useful but not essential, such as a version difference or a link
to a later chapter.
:::

::: {.warning title="Integer overflow"}
A warning box points out a mistake that compiles without complaint but produces wrong
answers, crashes, or a slow program. Many failed interview submissions come from the
mistakes in these boxes.
:::

::: {.tip title="Think aloud"}
An interview tip tells you what interviewers usually look for and how to explain your
reasoning so that they can follow it.
:::

::: {.example title="Choosing a structure"}
An example box works through a small concrete case, usually with real values.
:::

## Reading the code

All code in this book is Java. Each listing has a header that says what kind of code it is:

- **Complete program**: compiles and runs as shown. Its file name appears in the header.
- **Method**: one method (or a few) taken from a complete program; the file name shows
  where it lives.
- **Fragment**: a few lines that show a point. A fragment is not a complete program.
- **Pseudocode**: an algorithm written in plain steps; it is not Java.
- **Output** and **Input**: what a program printed, and what it read, when the book was
  built.

Listings have line numbers so that the explanations can refer to them. Here is a short
complete program.

```{.java include="code-examples/ch00/ReverseInPlace.java" #lst-reverse-program}
```

When the book was built, this program printed:

```{.output from="code-examples/ch00/ReverseInPlace"}
```

To run any program yourself, open a terminal in the folder that contains it and type the
two commands below. The first compiles the source code into bytecode; the second starts
the Java Virtual Machine (JVM) and runs the `main` method.

```{.console}
javac ReverseInPlace.java
java ReverseInPlace
```

## A sample section: reversing an array in place

This short section shows how a typical topic looks inside a chapter. The task is to reverse
an array without creating a second array.

**The idea.** Put one index, `left`, at the start and another, `right`, at the end. Swap
the two values, then move both indices one step toward the middle. Stop when they meet.

```{.diagram caption="Two pointers moving toward the middle"}
 index:    0     1     2     3     4
         +-----+-----+-----+-----+-----+
 values: |  3  |  8  |  1  |  9  |  4  |
         +-----+-----+-----+-----+-----+
            ^                       ^
          left                    right      swap, then left++ and right--
```

The method below is the `reverse` method from the program above, shown on its own.

```{.java include="code-examples/ch00/ReverseInPlace.java" region="reverse"}
```

**Line by line.** Line 3 and line 4 place the two indices at both ends. The loop on line 5
runs while `left` is still before `right`; when they meet or cross, every pair has been
swapped. Lines 6 to 8 swap two values through the temporary variable `temp`, because
writing `values[left] = values[right]` first would lose the old value of `values[left]`.
Lines 9 and 10 move both indices inward.

**Dry run.** For the input `[3, 8, 1, 9, 4]`:

| Step | left | right | Swap | Array after the step |
|------|------|-------|------|----------------------|
| start | 0 | 4 | – | [3, 8, 1, 9, 4] |
| 1 | 0 | 4 | 3 ↔ 4 | [4, 8, 1, 9, 3] |
| 2 | 1 | 3 | 8 ↔ 9 | [4, 9, 1, 8, 3] |
| stop | 2 | 2 | – | `left < right` is false, so the loop ends |

: Dry run of `reverse` on [3, 8, 1, 9, 4]

**Complexity.** Each loop iteration does a constant amount of work and moves both indices,
so the loop runs about *n*/2 times for an array of length *n*.

| Measure | Cost | Why |
|---------|------|-----|
| Time | O(n) | about n/2 iterations, each O(1) |
| Extra space | O(1) | only `left`, `right` and `temp`, whatever the array size |

: Cost of reversing in place

::: {.warning title="Off-by-one"}
Writing `while (left <= right)` also works here, because swapping the middle element with
itself changes nothing. But in many two-pointer algorithms the difference between `<` and
`<=` decides whether the answer is right. Always ask: *what should happen when the two
pointers meet?*
:::

## Exercises and solutions

Exercises are numbered by chapter and marked Easy, Medium, Hard or Challenge. Try each one
before you look at its solution. The solutions are collected at the end of each chapter; a
link under every exercise takes you there, and a link on every solution takes you back.

::: {.exercise number="0.1" level="Easy" title="Reverse part of an array"}
Change `reverse` so that it reverses only the part of the array between index `from` and
index `to`, inclusive, and leaves the rest unchanged. What should your method do when
`from > to`?
:::

::: {.solution number="0.1"}
Start the pointers at `from` and `to` instead of `0` and `values.length - 1`; the loop
body stays the same. When `from > to` the loop condition `left < right` is false at the
start, so the method changes nothing, which is a sensible result for an empty range. The
method should still reject indices outside the array, for example by throwing an
`IndexOutOfBoundsException` before the loop starts.
:::

## The companion files {.optional}

The book was built from a folder of source files that you can use directly:

| Folder or file | Contents |
|----------------|----------|
| `code-examples/` | every program in the book, by chapter |
| `exercises/` | the exercises of each chapter, as separate files |
| `solutions/` | worked solutions, with solution programs by chapter |
| `study_roadmap.md` | the 12-week and 16-week study plans |
| `references.md` | sources and further reading |
| `validation-report.md` | what was compiled, run and checked when the PDF was built |

: Companion files

The `README.md` file in that folder explains how to rebuild this PDF after you change
anything.
