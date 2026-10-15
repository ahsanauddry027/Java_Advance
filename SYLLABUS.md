# SYLLABUS.md — Scope, order, tiers and page budgets

**Tags:** **[Core]** full 14-part treatment · **[Std]** compact · **[Opt]** optional, labelled
in the PDF. Untagged items take the chapter's default tier. **Home** marks the chapter that
teaches a topic fully; other chapters cross-reference it.

**Page budgets** are targets for A4 at about 10.5 pt body text; ±20% is fine. If a chapter
truly needs much more, say so in `PROGRESS.md` instead of silently cutting or padding.
As written, the whole book comes to roughly 950–1,300 pages.

## Changes from the original prompt, and why

1. **Complexity analysis moved before Collections** (now Ch 6; Collections is now Ch 7).
   The collections chapter needs Big-O to explain anything.
2. **Lambda and method-reference essentials added to Ch 2.** Comparators,
   `computeIfAbsent` and `PriorityQueue` constructors use lambdas from the first DSA
   chapter onward. Ch 22 goes deeper (functional interfaces, streams).
3. **Heap sort moved to the Heaps chapter** (Ch 15). Ch 12 lists it in the sorting
   comparison table with a forward reference.
4. **Bit manipulation and math (now Ch 19) placed before Dynamic Programming (now Ch 20).**
   Bitmask DP and modular arithmetic are needed in DP.
5. **One home per topic:** generics, `equals`/`hashCode`, `Comparable`/`Comparator`, enums
   and records → Ch 3. HashMap internals → Ch 7. `Optional` → Ch 22. Fast I/O and OA
   templates → Ch 2. JUnit and stress testing → Ch 4.
6. **Chapter 30 solutions:** full worked solutions for all coding exercises and at least 40
   DSA prompts; approach + complexity + pitfalls for the rest, each linked to the section
   where the technique is taught. (Full solutions for all 100 would repeat Chapters 8–21.)

---

## PART I — Java for Problem Solving

### Ch 1 — Modern Java essentials for DSA · default [Std] · 25–35 pp
- Diagnostic quiz on prerequisite Java, with answers, to find gaps.
- Source code → bytecode → JVM; JDK vs JRE vs JVM; the compile-and-run process.
- Java versions and LTS releases (21, 25); what online judges typically run (verify, cite).
- [Core] Primitive vs reference types.
- [Core] Type conversion, casting, overflow, integer division, floating-point precision.
- [Core] `==` vs `.equals()`.
- [Core] `String`, `StringBuilder`, `StringBuffer`; immutability; the string pool.
- `final`, `static`, `this`, `super`, only as they matter in problem solving.
- `null`, `NullPointerException`, safe reference handling.
- Arrays: 1D, 2D, jagged; copying (`clone`, `Arrays.copyOf`, `System.arraycopy`).

### Ch 2 — I/O, standard-library tools and lambda essentials · default [Core] · 30–40 pp
Home of fast I/O and OA templates.
- `Scanner` vs `BufferedReader`; `StringTokenizer`; a custom fast reader.
- `StringBuilder` and `PrintWriter` for fast output; formatting output correctly.
- Parsing ints, longs and strings; multiple test cases; reading until EOF.
- Handling large input; common online-judge I/O errors.
- Choosing numeric types: `int`, `long`, `double`, [Std] `BigInteger` / `BigDecimal`.
- [Std] `Arrays` and `Collections` utilities used in DSA.
- **Lambda and method-reference essentials for DSA:** lambda syntax, target typing,
  comparator lambdas, `Comparator.comparing…` / `thenComparing` / `reversed`, method
  references (`Integer::compare`, `ArrayList::new`), lambdas in `computeIfAbsent` / `merge`.
- Sorting arrays and lists with built-in APIs; custom sorting; safe comparators.
- Reusable templates: single test, multiple tests, read-until-EOF, grid input. Every line
  explained. Ch 24 reuses them.

### Ch 3 — Object-oriented Java at an advanced level · default [Std] · 30–40 pp
- Inheritance vs interfaces vs abstract classes vs composition.
- Overloading vs overriding; static binding vs dynamic dispatch; runtime method resolution.
- [Core] `equals()` and `hashCode()` contracts. Home.
- [Core] `Comparable` vs `Comparator`. Home.
- Immutability and defensive copying; access modifiers and encapsulation.
- [Core] Generics: generic classes and methods, bounded type parameters, wildcards
  (PECS), type erasure and its consequences (no `new T[]`, no primitive type arguments). Home.
- Inner, static nested, anonymous and local classes (and why `Node` is usually `static`).
- Enums and records (home). Records as pair/tuple keys in DSA.
- When OOP matters in DSA and interviews; when a plain procedural solution is better.

### Ch 4 — Exceptions, debugging, testing and code quality · default [Std] · 20–30 pp
- Checked vs unchecked; `try` / `catch` / `finally` / `throw` / `throws`; try-with-resources.
- Meaningful custom exceptions: when and how.
- Reading stack traces; a systematic debugging method.
- Assertions (`-ea`) and defensive programming.
- [Core] JUnit 5 fundamentals. Home (Ch 27 extends to integration tests).
- [Core] Stress testing: brute force vs optimized on random inputs. Home.
- Readable, maintainable Java; common compiler and runtime errors.
- Debugging wrong DSA solutions: off-by-one, overflow, broken invariant, unintended mutation.

## PART II — Java Internals, Complexity and Collections

### Ch 5 — JVM, memory and performance · default [Std] · 25–35 pp
- JVM architecture; class loading and bytecode verification.
- Stack frames, local variables, method calls; heap allocation; references and object lifetime.
- Garbage collection and reachability (conceptual; no GC tuning).
- [Core] `StackOverflowError` vs `OutOfMemoryError`; recursion and stack usage.
- [Core] Autoboxing/unboxing; wrapper caching; `Integer` comparison pitfalls.
- Object overhead; `int[]` vs `List<Integer>` in memory and speed.
- Big-O vs real runtime: constant factors, cache locality, boxing, JIT warm-up (conceptual).
- State clearly that memory diagrams are conceptual and JVM implementations differ.

### Ch 6 — Complexity analysis · default [Core] · 25–35 pp
- Time and space complexity; Big O, Big Omega, Big Theta; best, average, worst case.
- Common growth rates with examples; amortized analysis (dynamic-array doubling).
- Nested and dependent loops; recursion complexity (recurrences, recursion trees,
  [Std] Master theorem).
- Auxiliary vs total space.
- Analyzing code line by line; comparing multiple solutions to one problem.
- Estimating whether an algorithm fits a time limit: a constraint → target-complexity table
  (labelled as a rough heuristic).
- Many exercises where I determine the complexity myself.

### Ch 7 — Java Collections Framework · default [Core] · 45–60 pp
- Hierarchy: `Iterable`, `Collection`, `List`, `Set`, `Queue`, `Deque`, `Map`.
- For each of `ArrayList`, `LinkedList`, `ArrayDeque`, `PriorityQueue`, `HashSet`,
  `LinkedHashSet`, `TreeSet`, `HashMap`, `LinkedHashMap`, `TreeMap`: internal structure,
  complexity (average / amortized / worst, each labelled), ordering, null rules, duplicates,
  use cases, interview uses.
- HashMap internals: hashing, buckets, resizing, collisions, tree bins (conceptual; labelled
  as an OpenJDK implementation detail). Home.
- `TreeMap` / `TreeSet` navigation API: `floorKey`, `ceilingKey`, `headMap`, `pollFirst`, …
- Iterators and enhanced `for`; fail-fast iterators and `ConcurrentModificationException`;
  `Iterator.remove()`; `removeIf`.
- `Map.Entry`; frequency-map idioms (`merge`, `getOrDefault`, `computeIfAbsent`).
- Custom objects as keys and elements; the mutable-key bug.
- `List.of` / `Map.of`: immutability and null rules.
- One large comparison table of all collections plus a "which collection?" decision guide.

## PART III — Data Structures and Algorithms (the most important part)

Explain both the abstract structure/algorithm and its Java implementation.

### Ch 8 — Arrays and strings · [Core] · 40–55 pp
- Traversal, insertion, deletion, searching, updating; 2D arrays and matrix traversal
  (spiral, rotation).
- Prefix and suffix sums (home); difference arrays (home); frequency counting.
- Two pointers (home); sliding window, fixed and variable (home).
- Maximum subarray sum and Kadane's algorithm; in-place algorithms.
- String traversal and manipulation; character frequency (`int[26]` vs map).
- Anagrams and palindrome checks; substrings vs subsequences; efficient concatenation.
- Common array and string interview patterns.

### Ch 9 — Linked lists · [Core] · 25–35 pp
- Singly, doubly and circular lists; node classes and references.
- Insertion and deletion; reversal, iterative and recursive.
- Fast and slow pointers; Floyd's cycle detection; finding the middle.
- Merging sorted lists; removing the Nth node from the end; dummy/sentinel nodes.
- Java-specific implementation choices and memory considerations.

### Ch 10 — Stacks, queues and deques · [Core] · 25–35 pp
- Stack and queue fundamentals; implementing both.
- `Deque` and `ArrayDeque`; why legacy `Stack` is usually not preferred.
- Balanced parentheses; stack-based expression processing.
- Monotonic stacks (home): next greater / next smaller element.
- Monotonic queues (home): sliding-window maximum.
- Queue-based BFS (preview; home is Ch 17).
- Queue using stacks; stack using queues.

### Ch 11 — Hashing in problem solving · [Core] · 20–30 pp
Problem-solving focus; internals are in Ch 7.
- Hash functions and hash tables (brief recap with reference); collision handling recap.
- Frequency counting; duplicate detection.
- Two Sum and related problems; grouping anagrams.
- Prefix sums + hashing; subarray sum equals K; longest consecutive sequence.
- Custom keys and equality contracts; common hashing pitfalls.

### Ch 12 — Searching and sorting · [Core] · 35–50 pp
- Linear and binary search; lower bound and upper bound; first and last occurrence.
- Search in rotated sorted arrays; binary search on the answer (home).
- Merge sort; quick sort (pivot choice, worst case).
- [Std] Insertion, selection and bubble sort, for comparison.
- [Std] Counting sort and radix sort, with their assumptions.
- Stability, in-place behavior, auxiliary space; comparison table (heap sort → Ch 15).
- `Arrays.sort()` and `Collections.sort()`: what the API docs guarantee vs implementation
  notes, for primitives vs objects. Verify against current JDK docs before stating details.
- Comparator-based sorting; sorting custom objects; choosing the right approach.
- For each algorithm: pseudocode when useful, Java implementation, dry run, complexity,
  stability, common mistakes.

### Ch 13 — Recursion and backtracking · [Core] · 35–45 pp
- Base and recursive cases; call stack and stack frames; recursion trees.
- Recursion vs iteration; tail recursion and Java's lack of guaranteed tail-call optimization.
- Subsets and subsequences; permutations; combinations; Combination Sum.
- N-Queens; [Std] Sudoku solver; word search.
- Pruning and constraint checking; avoiding duplicate results.
- Complexity analysis for branching algorithms.

### Ch 14 — Trees and binary search trees · [Core] · 45–60 pp
- Terminology and representations; binary trees and BSTs; node classes in Java.
- Preorder, inorder, postorder, level order; recursive and iterative versions.
- Height and depth; balanced trees (concept).
- BST search, insertion, deletion; validate a BST.
- Lowest common ancestor; diameter; maximum path sum.
- [Std] Serialize and deserialize.
- Tree recursion patterns; complexity; skewed trees and recursion depth in Java.

### Ch 15 — Heaps and priority queues · [Core] · 30–40 pp
- Min-heaps and max-heaps; array representation.
- Insertion, removal, heapify, build-heap (and why build-heap is O(n)).
- Heap sort (moved here from Ch 12).
- Java `PriorityQueue`; a safe max-heap comparator.
- Kth largest/smallest; top K frequent; merge K sorted lists; K closest points.
- Two-heaps pattern; running median.
- Complexity and common comparator mistakes.

### Ch 16 — Tries and advanced data structures · default [Std] · 35–50 pp
- [Core] Trie (array children vs map children); prefix search; autocomplete; word
  dictionary with wildcards.
- [Core] Disjoint Set Union / Union-Find with path compression and union by rank/size. Home.
- [Core] LRU cache with `HashMap` + doubly linked list; also the `LinkedHashMap` version.
- [Opt] Segment tree (range sum, point update); [Opt] Fenwick tree; [Opt] sparse table.
- When these are worth learning for interviews vs competitive programming; complexity table.

### Ch 17 — Graphs · [Core] · 55–75 pp
- Terminology; directed, undirected, weighted, unweighted.
- Adjacency list vs matrix; choosing a Java representation and its memory cost.
- BFS and DFS (recursive and iterative); connected components; unreachable vertices.
- Cycle detection (directed and undirected); bipartite check.
- Topological sort (DFS and Kahn's algorithm); course scheduling / dependency problems.
- Shortest paths: BFS (unweighted); Dijkstra (with `PriorityQueue`, lazy deletion, why
  negative edges break it); [Std] Bellman-Ford; [Std] Floyd-Warshall; [Opt] 0-1 BFS.
- [Std] Minimum spanning trees: Prim's and Kruskal's (DSU from Ch 16).
- Grid/matrix graphs; flood fill; counting islands.
- For each algorithm: assumptions, correctness intuition, data structures, Java
  implementation, complexity, failure cases.

### Ch 18 — Greedy algorithms · default [Core] · 20–30 pp
- Greedy choice property and optimal substructure; how to justify a greedy solution
  (exchange argument).
- Sorting-based greedy; interval scheduling; activity selection; meeting rooms;
  minimum platforms.
- Jump Game; Gas Station; fractional knapsack.
- [Opt] Huffman coding.
- Why greedy sometimes fails, with counterexamples (e.g., coin change with non-standard coins).

### Ch 19 — Bit manipulation and mathematical algorithms · default [Std] · 20–30 pp
- Binary representation; two's complement; Java's signed integers; `>>` vs `>>>`.
- AND, OR, XOR, NOT, shifts, masks; set / clear / toggle / check a bit.
- Counting set bits (`Integer.bitCount` and manual); power-of-two check; XOR problems.
- Bitmask representations of sets (used by bitmask DP in Ch 20).
- GCD and LCM; prime checking; sieve of Eratosthenes.
- [Core] Modular arithmetic (the `1_000_000_007` pattern, `Math.floorMod`, `long` products).
- [Core] Fast exponentiation; overflow-safe arithmetic (`Math.addExact` etc.).
- When mathematical shortcuts help in interviews.

### Ch 20 — Dynamic programming · [Core] · 60–80 pp
- Overlapping subproblems and optimal substructure; recognizing when DP applies.
- Recursion → memoization → tabulation → space optimization.
- **How to derive a recurrence:** state definition, transition, base cases, iteration order,
  answer extraction. Every DP problem in the chapter states all five explicitly.
- Fibonacci; climbing stairs; House Robber; Coin Change (min coins and number of ways).
- 0/1 knapsack and unbounded knapsack; subset sum; partition problems.
- Longest increasing subsequence; longest common subsequence; edit distance; string DP.
- Grid path problems.
- [Std] Interval DP fundamentals; [Opt] bitmask DP / state compression.
- Reconstructing an optimal solution; complexity; common DP mistakes and debugging
  (printing the table, checking small cases by hand).

### Ch 21 — Advanced interview patterns (synthesis) · 25–35 pp
- Pattern-recognition guide: from problem statement and constraints → target complexity →
  candidate patterns → rejecting unsuitable approaches → final choice.
- A "signals" table for every pattern (prefix sums, difference arrays, sweep line,
  intervals, fast/slow pointers, sliding window, binary search on answer, monotonic
  stack/queue, top K, K-way merge, tree DFS/BFS, graph traversal, backtracking, DP,
  Union-Find, in-place linked-list manipulation) with cross-references to home sections.
- Home of: [Std] sweep line; [Std] merging intervals; [Std] state-machine problems.
- 10–15 mixed worked problems showing the selection process, including problems that
  combine patterns.

## PART IV — Modern Java Features Worth Knowing

### Ch 22 — Lambdas, functional interfaces and streams · default [Std] · 25–35 pp
Builds on Ch 2's lambda essentials.
- Functional interfaces; lambda syntax and type inference; method references.
- `Predicate`, `Function`, `Consumer`, `Supplier` (and `BiFunction`, `UnaryOperator`).
- Stream pipelines; `map`, `filter`, `flatMap`, `reduce`, `collect`.
- Collectors: grouping, partitioning, counting; sorting streams; primitive streams.
- `Optional` and its appropriate use. Home.
- Lazy evaluation and terminal operations; side effects; debugging streams.
- When streams improve readability; when plain loops are better for DSA and
  performance-sensitive code. Never imply streams are faster.

### Ch 23 — Other useful Java features · default [Std] · 20–30 pp
- Switch expressions; text blocks; `var` and its limits (introduced here).
- Pattern matching for `instanceof` and `switch`, record patterns, sealed types (Java 21).
- Date and time API fundamentals.
- Concurrency introduction (home): threads, executors, synchronization, race conditions,
  deadlocks (conceptual); concurrent collections.
- [Opt] Virtual threads. [Opt] Java 25 highlights (clearly labelled).
- Table: which features matter in coding interviews vs backend engineering.

## PART V — Writing Reliable Java Solutions in Online Assessments

### Ch 24 — Online judge strategy · default [Std] · 15–25 pp
- A reusable solution template (from Ch 2); multiple tests; fast I/O; empty input and EOF.
- Edge-case analysis; integer overflow; time and memory limits.
- Recursion limits and workarounds (iterative rewrite; running in a thread with a larger stack).
- Arrays vs collections; avoiding unnecessary object creation.
- Interpreting WA, TLE, MLE and RE, with Java-specific causes.
- Debugging hidden-test failures; testing before submission (stress testing from Ch 4).

### Ch 25 — Problem-solving workflow for LeetCode and interviews · default [Std] · 15–20 pp
- Reading carefully; extracting constraints; brute force first; improving it.
- Explaining trade-offs; arguing correctness; complexity analysis.
- Writing clean Java; testing edge cases; explaining aloud (with a sample script).
- Handling hints and follow-ups; revisiting failed problems.
- Spaced repetition and a revision log template; how to avoid memorizing solutions.
- A one-page structured problem-solving checklist.

## PART VI — Software Engineering Job Preparation in Bangladesh, 2026

### Ch 26 — Java interview fundamentals (synthesis) · 30–40 pp
For each topic: a beginner-friendly model answer, deeper follow-up questions, common
misconceptions, a practical example. Cross-reference home chapters instead of re-teaching.
- OOP and SOLID principles (SOLID home here); basic design patterns (home: Singleton,
  Factory, Builder, Strategy, Observer).
- Java memory model (introductory); `equals`/`hashCode`; string immutability.
- Collections internals; exceptions; generics; garbage collection; JVM and JIT concepts.
- Threads and synchronization.
- SQL and database fundamentals (home); HTTP and REST basics (home); Git fundamentals (home).
- Testing and debugging; clean code and code review; DSA under time pressure.

### Ch 27 — Java for backend engineering · default [Std] · 45–60 pp
Secondary to DSA but important for employability. Build **one small, coherent REST API
project in stages** (e.g., a study-tracker or library API). Explain every important file,
dependency, annotation, class, method and the request flow. Never dump a whole app at once.
1. Maven project structure, dependency management, logging, JUnit (Gradle compared briefly).
2. JDBC with PostgreSQL (or MySQL): connections, prepared statements, transactions, ACID.
3. Spring Boot fundamentals: IoC and dependency injection; controllers, services,
   repositories; DTOs and validation; REST and JSON; HTTP methods and status codes.
4. [Std] Spring Data repositories.
5. Unit and integration tests; API testing.
6. Configuration and environment variables; authentication vs authorization (concepts);
   basic security practices.
7. Git workflow; [Opt] basic deployment concepts.
- Building this needs internet access for dependencies: ask me first. Anything not built
  is marked "not compiled" in the validation report.
- Keep it an introduction to a practical path, not a replacement for a Spring Boot book.

### Ch 28 — Bangladesh-focused job preparation · 15–25 pp
- Common types of software roles in Bangladesh; DSA-heavy hiring vs practical-development
  interviews.
- How Java, DSA, databases, APIs, Git, testing and projects contribute to employability.
- Preparing for online assessments, coding rounds, technical and HR interviews.
- Strategies for local companies, startups, IT service companies and multinationals.
- How to analyze current job descriptions and adjust preparation (use the ones in
  `research/job-descriptions/`; cite official career pages with access dates).
- Skills to demonstrate in a CV, GitHub portfolio and interviews; Java project ideas that
  go beyond basic CRUD.
- Discussing academic projects, teamwork, trade-offs and debugging stories.
- Prioritizing courses, DSA and projects as a CSE student.
- Every claim is marked **Verified observation** (source, date) or **General guidance**.
  No fabricated company questions; no promise that a roadmap guarantees a job.

## PART VII — Practice, Revision and Assessment

### Ch 29 — Topic-wise practice problems · 25–35 pp
- Per major DSA topic, grouped into Foundation / Intermediate / Advanced / Mixed-pattern.
- For each problem: title; link with verification status; difficulty; pattern; expected
  learning outcome; prerequisites (section references); a hint that doesn't give the
  solution away; **Essential** or **Extra** (the roadmap assigns Essential ones).
- Prefer canonical, public problems (LeetCode, HackerRank, Codeforces, GeeksforGeeks).
  Summaries in your own words only.

### Ch 30 — Interview question bank · 60–90 pp
- ≥100 Java/JVM conceptual questions with concise answers.
- ≥100 DSA problem prompts across topics and difficulty levels.
- ≥30 Java coding exercises (syntax, collections, debugging, implementation).
- ≥30 follow-up questions (complexity, edge cases, alternatives, design decisions).
- Separate solutions section: full worked solutions for all coding exercises and at least
  40 DSA prompts; approach + complexity + pitfalls with a section reference for the rest.
- Every question tagged with its home section. Quality over padding.

### Ch 31 — Revision handbook · 25–35 pp
Java syntax reference for DSA; collection selection guide; collection and algorithm
complexity tables; sorting comparison; graph-algorithm selection guide; DP
recurrence-building checklist; comparator patterns; common Java pitfalls; integer-overflow
checklist; I/O templates; frequently used library methods; interview answer frameworks;
one-page pattern-recognition sheet; final pre-interview checklist. All cross-referenced.

## Back matter
- Appendix A: Environment setup (JDK 21, IDE, compiling and running from the command line).
- Appendix B: Study roadmap (same content as `study_roadmap.md`).
- Glossary of technical terms.
- Bibliography and further reading (working URLs, access dates).
- Index, if the toolchain supports it.
- Version label and generation date.

---

## Study roadmap requirements (`study_roadmap.md` and Appendix B)

- Three tracks: **A** DSA and online assessments · **B** Java technical interviews ·
  **C** backend job readiness.
- A 12-week and a 16-week schedule at 2–3 h/day, 5–6 days/week (about 150–250 hours).
  This cannot cover the whole book; assign **Essential** sections and problems, and list
  optional material separately.
- For each week: concepts (by section number), coding exercises, problems (by Ch 29 ID),
  revision tasks, a measurable outcome, and a checkpoint or mock interview.
- Rules for adapting the plan when I'm ahead or behind. No promises of mastery or a job.

## PDF design requirements

A4; professional typography and consistent spacing; cover page with title, subtitle and
edition date; clickable table of contents; numbered parts, chapters and sections; page
numbers and running headers; distinct styles for definitions, warnings, interview tips,
code, examples and exercises; Java syntax highlighting; readable code font; code blocks that
never clip and break cleanly across pages; well-formatted tables; consistent diagrams with
captions; no clipped text, overlaps, missing glyphs or wasted blank space; readable on
screen and in print.

## Final deliverables

1. `Advanced_Java_DSA_Job_Preparation_2026.pdf`: the primary deliverable.
2. `README.md`: how to use the materials and rebuild the PDF.
3. `study_roadmap.md`.
4. `code-examples/`: organized, runnable Java examples.
5. `exercises/` and `solutions/`: topic-wise practice and worked solutions.
6. `references.md` and `references/problems.csv`.
7. `validation-report.md` (a copy is also written to `build/`): what was compiled, tested
   and checked (from real output), and what remains unverified.

## Final acceptance criteria

- Every chapter above has substantive content at its tiers and within ±20% of its budget,
  or the deviation is explained in `PROGRESS.md`.
- `scripts/build` passes: all code compiles with `--release 21`, all self-checks pass, the
  check script is clean.
- Every chapter's pages have been rendered and visually inspected at least once.
- Interview-essential vs optional material is clearly marked.
- Bangladesh-specific claims are sourced or explicitly marked as general guidance.
- All deliverables exist; the final report gives the PDF's real path, size and page count.
- Limitations are documented honestly.
