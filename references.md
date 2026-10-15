# References

Every web link below was checked (HTTP 200) on the date shown. Official Java documentation
is for Java SE 21 unless a different version is named. Where the book describes how
OpenJDK *implements* something, the claim refers to the OpenJDK 21 source code and may
change in other versions; the specification and API documentation are the guarantees.

## Java language, JVM and tools

- Oracle. *The Java Language Specification, Java SE 21 Edition.*
  <https://docs.oracle.com/javase/specs/jls/se21/html/index.html> (checked 2026-10-15)
- Oracle. *The Java Virtual Machine Specification, Java SE 21 Edition.*
  <https://docs.oracle.com/javase/specs/jvms/se21/html/index.html> (checked 2026-10-15)
- Oracle. *The javac Command* (JDK 21 tool reference).
  <https://docs.oracle.com/en/java/javase/21/docs/specs/man/javac.html> (checked 2026-10-15)
- Oracle. *The java Command* (JDK 21 tool reference).
  <https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html> (checked 2026-10-15)
- OpenJDK. *JDK 21* project page (release contents and schedule).
  <https://openjdk.org/projects/jdk/21/> (checked 2026-10-15)
- OpenJDK. *JDK 25* project page (release contents and schedule).
  <https://openjdk.org/projects/jdk/25/> (checked 2026-10-15)
- Oracle. *Oracle Java SE Support Roadmap* (LTS releases and support dates).
  <https://www.oracle.com/java/technologies/java-se-support-roadmap.html> (checked 2026-10-15)

## Java Enhancement Proposals (JEPs)

- JEP 254: Compact Strings (Java 9). <https://openjdk.org/jeps/254> (checked 2026-10-15)
- JEP 280: Indify String Concatenation (Java 9). <https://openjdk.org/jeps/280>
  (checked 2026-10-15)
- JEP 330: Launch Single-File Source-Code Programs (Java 11).
  <https://openjdk.org/jeps/330> (checked 2026-10-15)
- JEP 358: Helpful NullPointerExceptions (Java 14). <https://openjdk.org/jeps/358>
  (checked 2026-10-15)
- JEP 400: UTF-8 by Default (Java 18). <https://openjdk.org/jeps/400> (checked 2026-10-15)
- JEP 431: Sequenced Collections (Java 21). <https://openjdk.org/jeps/431>
  (checked 2026-10-15)
- JEP 512: Compact Source Files and Instance Main Methods (Java 25).
  <https://openjdk.org/jeps/512> (checked 2026-10-15)

## Java language specification chapters used

- JLS 21, Chapter 3 (Lexical Structure: string literals and interning).
  <https://docs.oracle.com/javase/specs/jls/se21/html/jls-3.html> (checked 2026-10-15)
- JLS 21, Chapter 5 (Conversions and Contexts: widening, narrowing, boxing).
  <https://docs.oracle.com/javase/specs/jls/se21/html/jls-5.html> (checked 2026-10-15)
- JLS 21, Chapter 15 (Expressions: integer division, remainder, evaluation order).
  <https://docs.oracle.com/javase/specs/jls/se21/html/jls-15.html> (checked 2026-10-15)

## Java API documentation (Java SE 21)

- `java.lang.String`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/String.html>
  (checked 2026-10-15)
- `java.lang.StringBuilder`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StringBuilder.html>
  (checked 2026-10-15)
- `java.lang.StringBuffer`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/StringBuffer.html>
  (checked 2026-10-15)
- `java.lang.Integer` (including the `valueOf` caching guarantee):
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Integer.html>
  (checked 2026-10-15)
- `java.lang.Math` (`addExact`, `floorMod`, `round`):
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/lang/Math.html>
  (checked 2026-10-15)
- `java.util.Arrays`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Arrays.html>
  (checked 2026-10-15)
- `java.util.Objects`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Objects.html>
  (checked 2026-10-15)
- *The javap Command* (JDK 21):
  <https://docs.oracle.com/en/java/javase/21/docs/specs/man/javap.html> (checked 2026-10-15)

- `java.util.Deque`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Deque.html>
  (checked 2026-10-15)
- `java.util.ArrayDeque`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/ArrayDeque.html>
  (checked 2026-10-15)
- `java.util.Queue`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Queue.html>
  (checked 2026-10-15)
- `java.util.Stack`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Stack.html>
  (checked 2026-10-15)
- `java.util.Vector`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/Vector.html>
  (checked 2026-10-15)
- `java.util.LinkedList`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/LinkedList.html>
  (checked 2026-10-15)
- `java.util.PriorityQueue`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/PriorityQueue.html>
  (checked 2026-10-15)
- `java.util.NoSuchElementException`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/NoSuchElementException.html>
  (checked 2026-10-15)
- `java.util.concurrent.ArrayBlockingQueue`:
  <https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/concurrent/ArrayBlockingQueue.html>
  (checked 2026-10-15)

## OpenJDK source code

- OpenJDK. `ArrayDeque.java` in the JDK 21 updates repository (circular array, growth
  policy). <https://github.com/openjdk/jdk21u/blob/master/src/java.base/share/classes/java/util/ArrayDeque.java>
  (checked 2026-10-15)

## Algorithms and data structures

- Cormen, T. H., Leiserson, C. E., Rivest, R. L., and Stein, C. *Introduction to
  Algorithms*, 4th edition. MIT Press, 2022.
- Sedgewick, R., and Wayne, K. *Algorithms*, 4th edition. Addison-Wesley, 2011. Companion
  site: <https://algs4.cs.princeton.edu/home/> (checked 2026-10-15); stacks and queues:
  <https://algs4.cs.princeton.edu/13stacks/> (checked 2026-10-15)

## Java practice and style

- Bloch, J. *Effective Java*, 3rd edition. Addison-Wesley, 2018.

## Online judges

- HackerRank. *Execution Environment* (languages, versions, time and memory limits).
  <https://hackerrank-knowledge-base.help.usepylon.com/articles/6693750503>
  (checked 2026-10-15; the page said "Last updated 2 months ago")

## Tools used to build this book

- Pandoc User's Guide. <https://pandoc.org/MANUAL.html> (checked 2026-10-15)
- Typst documentation. <https://typst.app/docs/> (checked 2026-10-15)
