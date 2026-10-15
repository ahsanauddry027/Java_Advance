# PROGRESS

Status values: `todo` · `in progress` · `done`. A chapter is `done` only when it meets the
definition of done in CLAUDE.md (topics covered at their tier, code compiles and
self-checks pass, check script clean, PDF builds, pages rendered and inspected, committed).

**Next up:** Phase 1 pilot: Chapter 10 (Stacks, queues and deques), then Chapters 1–9 in order.

## Phase 0: setup

- [x] Environment inspected (2026-10-15): Python 3.14, git 2.55, Pandoc 3.12.1 and Typst
      0.15.1 installed with winget (approved by the user); JDK 25.0.3 from Android Studio
      (`C:\Program Files\Android\Android Studio\jbr`) compiles with `--release 21`.
- [x] Folder structure and git repository
- [x] Typst template (`templates/book.typ`) and Pandoc filter (`templates/book.lua`)
- [x] `scripts/build.py`, `scripts/check.py`, `scripts/extract.py`, `scripts/listing.py`
- [x] Style sample: cover, contents, preface, "How to Use This Book" (uses every element);
      pages rendered and inspected
- [x] `references.md` started (links checked 2026-10-15), `references/problems.csv` header

## Chapters

| Ch | Title | Budget (pp) | Status | Pages | Inspected |
|----|-------|-------------|--------|-------|-----------|
| 1 | Modern Java Essentials for DSA | 25–35 | todo | | |
| 2 | I/O, Standard-Library Tools and Lambda Essentials | 30–40 | todo | | |
| 3 | Object-Oriented Java at an Advanced Level | 30–40 | todo | | |
| 4 | Exceptions, Debugging, Testing and Code Quality | 20–30 | todo | | |
| 5 | JVM, Memory and Performance | 25–35 | todo | | |
| 6 | Complexity Analysis | 25–35 | todo | | |
| 7 | The Java Collections Framework | 45–60 | todo | | |
| 8 | Arrays and Strings | 40–55 | todo | | |
| 9 | Linked Lists | 25–35 | todo | | |
| 10 | Stacks, Queues and Deques | 25–35 | in progress | | |
| 11 | Hashing in Problem Solving | 20–30 | todo | | |
| 12 | Searching and Sorting | 35–50 | todo | | |
| 13 | Recursion and Backtracking | 35–45 | todo | | |
| 14 | Trees and Binary Search Trees | 45–60 | todo | | |
| 15 | Heaps and Priority Queues | 30–40 | todo | | |
| 16 | Tries and Advanced Data Structures | 35–50 | todo | | |
| 17 | Graphs | 55–75 | todo | | |
| 18 | Greedy Algorithms | 20–30 | todo | | |
| 19 | Bit Manipulation and Mathematical Algorithms | 20–30 | todo | | |
| 20 | Dynamic Programming | 60–80 | todo | | |
| 21 | Advanced Interview Patterns | 25–35 | todo | | |
| 22 | Lambdas, Functional Interfaces and Streams | 25–35 | todo | | |
| 23 | Other Useful Java Features | 20–30 | todo | | |
| 24 | Online Judge Strategy | 15–25 | todo | | |
| 25 | The Problem-Solving Workflow | 15–20 | todo | | |
| 26 | Java Interview Fundamentals | 30–40 | todo | | |
| 27 | Java for Backend Engineering | 45–60 | todo | | |
| 28 | Bangladesh-Focused Job Preparation | 15–25 | todo | | |
| 29 | Topic-Wise Practice Problems | 25–35 | todo | | |
| 30 | Interview Question Bank | 60–90 | todo | | |
| 31 | Revision Handbook | 25–35 | todo | | |

## Topic checklist

Tick a topic when it is written at its tier (see SYLLABUS.md for tiers and homes).

### Ch 1: Modern Java Essentials for DSA
- [ ] Diagnostic quiz with answers
- [ ] Source → bytecode → JVM; JDK/JRE/JVM; compile-and-run
- [ ] Java versions and LTS releases; what online judges run
- [ ] Primitive vs reference types
- [ ] Conversion, casting, overflow, integer division, floating-point precision
- [ ] `==` vs `.equals()`
- [ ] `String`, `StringBuilder`, `StringBuffer`; immutability; string pool
- [ ] `final`, `static`, `this`, `super` for problem solving
- [ ] `null`, `NullPointerException`, safe reference handling
- [ ] Arrays: 1D, 2D, jagged; copying

### Ch 2: I/O, Standard-Library Tools and Lambda Essentials
- [ ] `Scanner` vs `BufferedReader`; `StringTokenizer`; custom fast reader
- [ ] `StringBuilder` / `PrintWriter` output; formatting
- [ ] Parsing; multiple test cases; reading until EOF
- [ ] Large input; common online-judge I/O errors
- [ ] Choosing numeric types; `BigInteger` / `BigDecimal`
- [ ] `Arrays` and `Collections` utilities
- [ ] Lambda and method-reference essentials for DSA
- [ ] Sorting with built-in APIs; custom and safe comparators
- [ ] Reusable OA templates, every line explained

### Ch 3: Object-Oriented Java at an Advanced Level
- [ ] Inheritance vs interfaces vs abstract classes vs composition
- [ ] Overloading vs overriding; static vs dynamic dispatch
- [ ] `equals()` / `hashCode()` contracts (home)
- [ ] `Comparable` vs `Comparator` (home)
- [ ] Immutability, defensive copying, access modifiers
- [ ] Generics: classes, methods, bounds, wildcards, erasure (home)
- [ ] Inner, static nested, anonymous, local classes
- [ ] Enums and records (home)
- [ ] When OOP matters in DSA; when procedural is better

### Ch 4: Exceptions, Debugging, Testing and Code Quality
- [ ] Checked vs unchecked; try/catch/finally/throw/throws; try-with-resources
- [ ] Custom exceptions
- [ ] Stack traces; systematic debugging
- [ ] Assertions and defensive programming
- [ ] JUnit 5 fundamentals (home)
- [ ] Stress testing (home)
- [ ] Readable code; common compiler and runtime errors
- [ ] Debugging wrong DSA solutions

### Ch 5: JVM, Memory and Performance
- [ ] JVM architecture; class loading; bytecode verification
- [ ] Stack frames, heap, references, object lifetime
- [ ] Garbage collection and reachability
- [ ] `StackOverflowError` vs `OutOfMemoryError`; recursion depth
- [ ] Autoboxing, wrapper caching, `Integer` pitfalls
- [ ] Object overhead; `int[]` vs `List<Integer>`
- [ ] Big-O vs real runtime; JIT warm-up

### Ch 6: Complexity Analysis
- [ ] Time/space; O, Ω, Θ; best/average/worst
- [ ] Growth rates; amortized analysis
- [ ] Nested and dependent loops; recursion complexity; Master theorem
- [ ] Auxiliary vs total space
- [ ] Line-by-line analysis; comparing solutions
- [ ] Constraint → target-complexity table
- [ ] Many self-analysis exercises

### Ch 7: The Java Collections Framework
- [ ] Hierarchy: Iterable … Map
- [ ] ArrayList, LinkedList, ArrayDeque, PriorityQueue
- [ ] HashSet, LinkedHashSet, TreeSet
- [ ] HashMap (internals, home), LinkedHashMap, TreeMap
- [ ] TreeMap/TreeSet navigation API
- [ ] Iterators, fail-fast, `ConcurrentModificationException`, `Iterator.remove()`, `removeIf`
- [ ] `Map.Entry`; frequency-map idioms
- [ ] Custom keys; mutable-key bug
- [ ] `List.of` / `Map.of`
- [ ] Comparison table and decision guide

### Ch 8: Arrays and Strings
- [ ] Basic operations; 2D traversal (spiral, rotation)
- [ ] Prefix/suffix sums (home); difference arrays (home); frequency counting
- [ ] Two pointers (home); sliding window (home)
- [ ] Kadane's algorithm; in-place algorithms
- [ ] String manipulation; character frequency
- [ ] Anagrams; palindromes; substrings vs subsequences; concatenation
- [ ] Common patterns

### Ch 9: Linked Lists
- [ ] Singly, doubly, circular; nodes and references
- [ ] Insertion, deletion; reversal (iterative, recursive)
- [ ] Fast/slow pointers; Floyd's cycle detection; middle node
- [ ] Merge sorted lists; remove Nth from end; sentinel nodes
- [ ] Java-specific choices and memory

### Ch 10: Stacks, Queues and Deques
- [ ] Stack and queue fundamentals; implementing both
- [ ] `Deque` and `ArrayDeque`; why not legacy `Stack`
- [ ] Balanced parentheses
- [ ] Stack-based expression processing
- [ ] Monotonic stack (home): next greater / next smaller
- [ ] Monotonic queue (home): sliding-window maximum
- [ ] Queue-based BFS (preview)
- [ ] Queue using stacks; stack using queues

### Ch 11: Hashing in Problem Solving
- [ ] Recap of hash tables and collisions
- [ ] Frequency counting; duplicate detection
- [ ] Two Sum family; grouping anagrams
- [ ] Prefix sums + hashing; subarray sum equals K; longest consecutive sequence
- [ ] Custom keys; pitfalls

### Ch 12: Searching and Sorting
- [ ] Linear/binary search; lower/upper bound; first/last occurrence
- [ ] Rotated arrays; binary search on the answer (home)
- [ ] Merge sort; quick sort
- [ ] Insertion, selection, bubble sort
- [ ] Counting and radix sort
- [ ] Stability, in-place, auxiliary space; comparison table
- [ ] `Arrays.sort` / `Collections.sort`: guarantees vs implementation notes
- [ ] Comparator sorting; custom objects; choosing an approach

### Ch 13: Recursion and Backtracking
- [ ] Base/recursive cases; call stack; recursion trees
- [ ] Recursion vs iteration; tail calls in Java
- [ ] Subsets; permutations; combinations; Combination Sum
- [ ] N-Queens; Sudoku; word search
- [ ] Pruning; avoiding duplicates; branching complexity

### Ch 14: Trees and Binary Search Trees
- [ ] Terminology; representations; node classes
- [ ] Traversals, recursive and iterative; level order
- [ ] Height, depth, balance
- [ ] BST search/insert/delete; validate BST
- [ ] LCA; diameter; maximum path sum
- [ ] Serialize/deserialize
- [ ] Recursion patterns; skewed trees and recursion depth

### Ch 15: Heaps and Priority Queues
- [ ] Min/max heaps; array representation
- [ ] Insert, remove, heapify, build-heap
- [ ] Heap sort
- [ ] `PriorityQueue`; safe max-heap comparator
- [ ] Kth element; top K frequent; merge K lists; K closest points
- [ ] Two heaps; running median
- [ ] Comparator mistakes

### Ch 16: Tries and Advanced Data Structures
- [ ] Trie; prefix search; autocomplete; wildcard dictionary
- [ ] DSU with path compression and union by rank/size (home)
- [ ] LRU cache (HashMap + DLL, and LinkedHashMap)
- [ ] Segment tree (optional); Fenwick tree (optional); sparse table (optional)
- [ ] When worth learning; comparison table

### Ch 17: Graphs
- [ ] Terminology; representations and memory
- [ ] BFS, DFS; components; unreachable vertices
- [ ] Cycle detection; bipartite check
- [ ] Topological sort (DFS, Kahn); course scheduling
- [ ] BFS shortest path; Dijkstra; Bellman-Ford; Floyd-Warshall; 0-1 BFS (optional)
- [ ] MST: Prim, Kruskal
- [ ] Grid graphs; flood fill; islands

### Ch 18: Greedy Algorithms
- [ ] Greedy choice; exchange argument
- [ ] Interval scheduling; activity selection; meeting rooms; platforms
- [ ] Jump Game; Gas Station; fractional knapsack
- [ ] Huffman coding (optional)
- [ ] Counterexamples where greedy fails

### Ch 19: Bit Manipulation and Mathematical Algorithms
- [ ] Binary, two's complement, `>>` vs `>>>`
- [ ] Bit operations and masks
- [ ] Set bits; power of two; XOR problems; bitmask sets
- [ ] GCD/LCM; primes; sieve
- [ ] Modular arithmetic; fast exponentiation; overflow-safe arithmetic

### Ch 20: Dynamic Programming
- [ ] Recognizing DP; memoization → tabulation → space optimization
- [ ] Deriving recurrences (state, transition, base, order, answer)
- [ ] Fibonacci, stairs, House Robber, Coin Change
- [ ] Knapsack (0/1, unbounded); subset sum; partition
- [ ] LIS; LCS; edit distance; string DP
- [ ] Grid paths
- [ ] Interval DP; bitmask DP (optional)
- [ ] Reconstruction; debugging DP

### Ch 21: Advanced Interview Patterns
- [ ] Pattern-recognition guide and signals table
- [ ] Sweep line; merging intervals; state machines (homes)
- [ ] 10–15 mixed worked problems

### Ch 22: Lambdas, Functional Interfaces and Streams
- [ ] Functional interfaces; lambdas; method references
- [ ] Core functional interfaces
- [ ] Streams: map/filter/flatMap/reduce/collect; collectors
- [ ] `Optional` (home); primitive streams
- [ ] Laziness; side effects; debugging; streams vs loops

### Ch 23: Other Useful Java Features
- [ ] Switch expressions; text blocks; `var`
- [ ] Pattern matching; record patterns; sealed types
- [ ] Date/time API
- [ ] Concurrency introduction (home); concurrent collections
- [ ] Virtual threads (optional); Java 25 highlights (optional)
- [ ] Interview vs backend usefulness table

### Ch 24: Online Judge Strategy
- [ ] Template; multiple tests; EOF; edge cases; overflow
- [ ] Recursion limits; time/memory limits
- [ ] Verdicts and Java-specific causes
- [ ] Testing before submission

### Ch 25: The Problem-Solving Workflow
- [ ] Reading, constraints, brute force, optimization
- [ ] Correctness, complexity, clean code, testing
- [ ] Explaining aloud; hints; revisiting; spaced repetition
- [ ] One-page checklist

### Ch 26: Java Interview Fundamentals
- [ ] OOP and SOLID (home); design patterns (home)
- [ ] JMM intro; `equals`/`hashCode`; strings; collections; exceptions; generics; GC; JIT
- [ ] Threads and synchronization
- [ ] SQL (home); HTTP and REST (home); Git (home)
- [ ] Testing; clean code; code review; time pressure

### Ch 27: Java for Backend Engineering
- [ ] Stage 1: Maven, structure, logging, JUnit
- [ ] Stage 2: JDBC, PostgreSQL, transactions, ACID
- [ ] Stage 3: Spring Boot, DI, controllers/services/repositories, DTOs, validation, REST
- [ ] Stage 4: Spring Data
- [ ] Stage 5: unit, integration and API tests
- [ ] Stage 6: configuration, auth concepts, security basics
- [ ] Stage 7: Git workflow; deployment basics (optional)

### Ch 28: Bangladesh-Focused Job Preparation
- [ ] Role types; DSA-heavy vs practical interviews
- [ ] Preparation for OA, technical and HR rounds
- [ ] Employer-type strategies; analyzing job descriptions (needs files in research/job-descriptions/)
- [ ] CV, GitHub, project ideas; discussing projects
- [ ] Prioritization for a CSE student

### Ch 29: Topic-Wise Practice Problems
- [ ] Problem sets per topic (Foundation/Intermediate/Advanced/Mixed), with links and hints

### Ch 30: Interview Question Bank
- [ ] ≥100 Java/JVM conceptual questions with answers
- [ ] ≥100 DSA prompts
- [ ] ≥30 Java coding exercises
- [ ] ≥30 follow-up questions
- [ ] Solutions section

### Ch 31: Revision Handbook
- [ ] All reference tables, checklists and templates

## Back matter and final tasks

- [ ] Appendix A: environment setup
- [ ] Appendix B: study roadmap (+ `study_roadmap.md`)
- [ ] Glossary complete
- [ ] References complete (all links checked)
- [ ] Index terms marked in every chapter
- [ ] `README.md` final
- [ ] Final full build, validation report, version label 1.0

## Session log

- 2026-10-15: Phase 0 complete (toolchain, scripts, style sample). Pilot chapter started.
