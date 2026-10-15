# I/O, Standard-Library Tools and Lambda Essentials {#io-library-lambdas}

::: objectives
**After this chapter you will be able to:**

- read input quickly and correctly with `Scanner`, `BufferedReader` with
  `StringTokenizer`, and a hand-written fast reader, and explain why their speeds differ;
- write large outputs efficiently, flush them reliably, and format numbers in a way that
  does not depend on the computer's language settings;
- handle the standard input shapes: multiple test cases, reading until the end of the
  input, and character grids;
- use `BigInteger` and `BigDecimal` when `long` and `double` are not enough;
- use the most useful methods of `Arrays` and `Collections`;
- read and write lambdas and method references, and use them with `Map.merge` and
  `computeIfAbsent`;
- sort arrays and lists in any order with safe comparators;
- start every online assessment from a reusable, fully understood template.

**Prerequisites:** Chapter 1 (types, overflow, strings, arrays).
:::

::: quiz
1. Which is faster for reading a million integers: `Scanner` or `BufferedReader`? Why?
2. Your program prints its answers with a `PrintWriter` but the judge sees no output.
   What did you most likely forget?
3. What does `(a, b) -> a - b` do wrong as a comparator?
4. What does `Arrays.binarySearch` return when the key is not in the array?
5. What is the difference between `Integer::compare` and `(x, y) -> Integer.compare(x, y)`?
:::

Online judges give your program its input as text on *standard input* and read its
answer from *standard output*. When the input has a million numbers, reading it the slow
way can take longer than the whole time limit, even if your algorithm is perfect. This
chapter starts with input and output, then covers the library tools you will use in
almost every solution: big numbers, array and collection utilities, lambdas and
comparators. It ends with a template that puts all of it together.

## Reading input: Scanner and BufferedReader {#reading-input}

### Scanner

`java.util.Scanner` is the easiest way to read input. It splits the input into *tokens*
(pieces separated by whitespace) and converts them for you.

```{.java include="code-examples/ch02/ScannerSum.java" #lst-scanner}
```

```{.input from="code-examples/ch02/ScannerSum"}
```

```{.output from="code-examples/ch02/ScannerSum"}
```

`new Scanner(System.in)` (line 7) wraps standard input. `nextInt()` skips any whitespace,
including line breaks, reads the next token and converts it to an `int`, so it does not
matter how the numbers are spread over lines. The sum is a `long` (line 9) because the
sum of many `int` values can overflow an `int` (Chapter 1).

**Why it is slow.** The Scanner documentation describes it as a scanner that parses
"using regular expressions". Every `nextInt` call matches the input against patterns,
which is flexible and convenient but does much more work than the task needs. For a few
thousand numbers this does not matter. For a million, it does.

::: {.warning title="nextInt() followed by nextLine()"}
`nextInt()` reads the number but leaves the rest of the line, including the line break,
unread. A following `nextLine()` returns that empty rest of the line, not the next line.
If you mix the two, call `nextLine()` once more to skip the leftover line break, or read
everything with `nextLine()` and parse the numbers yourself.
:::

### BufferedReader and StringTokenizer

`java.io.BufferedReader` reads characters in large blocks and hands out whole lines.
You split each line into tokens and convert them yourself.

```{.java include="code-examples/ch02/BufferedSum.java" #lst-buffered}
```

```{.output from="code-examples/ch02/BufferedSum"}
```

**Syntax breakdown.**

- `new BufferedReader(new InputStreamReader(System.in))` (line 10) builds a chain:
  `System.in` delivers raw bytes, `InputStreamReader` decodes them into characters, and
  `BufferedReader` adds a buffer and the `readLine()` method.
- `throws IOException` (line 9) is required because `readLine` declares that checked
  exception (Chapter 4). In contest code, declaring it on `main` is the usual choice.
- `reader.readLine()` (line 11) returns the next line without its line break, or `null`
  at the end of the input. `trim()` removes stray spaces, which `Integer.parseInt` would
  reject.
- `new StringTokenizer(line)` (line 12) splits the line at whitespace;
  `nextToken()` (line 15) returns the tokens one by one.

`StringTokenizer` is officially a *legacy* class: its documentation discourages it in new
code and recommends `String.split` instead. In competitive programming it is still widely
used because it does not use regular expressions and is fast. `split("\\s+")` is fine when
speed does not matter.

This version assumes that all n numbers are on the second line. Many inputs spread
numbers over several lines; Exercise 2.1 builds a `next()` method that reads more lines
whenever the current one runs out of tokens.

### How much faster?

The program `IoBenchmark.java` in `code-examples/ch02` generates one million random
integers as text, ten per line, and reads them three times: with `Scanner`, with
`BufferedReader` and `StringTokenizer`, and with the fast reader of the next section. It
then prints a million numbers in three ways. When this book was built, it printed:

```{.output from="code-examples/ch02/IoBenchmark"}
```

The exact numbers depend on the computer and change from run to run; compare the ratios.
`BufferedReader` with `StringTokenizer` was several times faster than `Scanner`, and the
fast reader was faster still. All three computed the same sum, which the program checks.
The output part prints to a "null" stream that discards everything, so it measures only
the cost inside Java; with real output to a terminal or file, flushing after every line
costs even more, because each flush is a separate request to the operating system.

## A fast reader {#fast-reader}

When input is large and contains only numbers and simple words, you can do better than
any general-purpose class by reading raw bytes and converting digits yourself. This is the
[fast reader]{.idx} pattern used by many competitive programmers.

```{.diagram caption="The fast reader keeps a block of input bytes and an index into it"}
   input text:  "7 -12" line break "9000000000" ...   (read in 64 KB blocks)

   buffer:  [ '7' ' ' '-' '1' '2' '\n' '9' ... ]
                       ^
                   position            length = number of valid bytes
```

```{.java include="code-examples/ch02/FastReader.java" region="reader" kind="Fields and methods" #lst-fastreader}
```

```{.input from="code-examples/ch02/FastReader"}
```

```{.output from="code-examples/ch02/FastReader"}
```

**Line by line.**

- Lines 1–4: the reader holds the underlying `InputStream`, a byte array `buffer`, the
  number of valid bytes in it (`length`) and the index of the next unread byte
  (`position`).
- Lines 6–13: the public constructor uses a 64 KB buffer (`1 << 16` is 2^16^ = 65,536).
  A second, package-private constructor lets the self-check program use tiny buffers, to
  prove that numbers split across two blocks are still read correctly.
- Lines 16–27, `fill`: if unread bytes remain, there is nothing to do. Otherwise
  `in.read(buffer, 0, buffer.length)` reads up to a whole block and returns how many
  bytes it read, or -1 at the end of the input. The method returns whether at least one
  unread byte is now available.
- Lines 30–38, `hasNext`: skips whitespace. Every character with a code of 32 (`' '`) or
  less is treated as whitespace: spaces, tabs, `\n` and `\r`. `buffer[position] & 0xFF`
  converts the `byte`, which Java treats as signed (−128 to 127), to a value from 0 to
  255, so that non-ASCII bytes are not mistaken for whitespace.
- Lines 40–54, `nextLong`: after skipping whitespace, an optional minus sign is consumed
  (lines 44–47). Then each digit is added to the value: `value * 10 + (digit - '0')`
  (lines 49–52). The loop calls `fill()` before every byte, so a number that is cut in two
  by a block boundary is still read correctly.
- Line 57, `nextInt`: converts the `long` to an `int`. If the input breaks its stated
  constraints, the value is silently truncated; judges guarantee the constraints, so
  this is acceptable in contest code.
- Lines 60–70, `next`: reads a word, one byte per character.

**Dry run** of `nextLong` on the bytes `-12` followed by a line break:

| Step | Byte at `position` | Action | `value` |
|------|---------------------|-----------------------------|---------|
| 1 | `-` | set `negative = true`, advance | 0 |
| 2 | `1` | `value = 0 × 10 + 1` | 1 |
| 3 | `2` | `value = 1 × 10 + 2` | 12 |
| 4 | `\n` | not a digit: loop ends | 12 |
| end | | return `-value` | −12 |

: Parsing "-12" byte by byte

**Complexity.** Every input byte is examined a constant number of times, so reading the
whole input costs O(total input size), with O(1) extra memory beyond the fixed buffer.

**Limitations.** The reader treats every byte as one character, which is correct for
ASCII input, the usual case for numeric problems. For text in Bengali or other non-ASCII
scripts, use `BufferedReader`, which decodes characters properly. It also does not check
for malformed numbers or overflow. If you are allowed to choose, `BufferedReader` with
`StringTokenizer` is fast enough for almost every problem; a byte-level reader is the
safe choice when the input has millions of tokens.

## Writing output fast {#fast-output}

Output has the same problem as input. `System.out.println` is convenient, but every call
goes through a synchronized `PrintStream`, and the stream may flush its data to the
operating system often. The standard fixes:

1. **Collect the output in a `StringBuilder`** and print it once at the end. This is
   simple and fast, but it holds the whole output in memory.
2. **Use a `PrintWriter` over a `BufferedOutputStream`** and call `flush()` once at the
   end. Output is written in large blocks while the program runs, so memory stays small.

::: {.warning title="Always flush"}
A `PrintWriter` created as `new PrintWriter(new BufferedOutputStream(System.out))` keeps
its data in memory until its buffer fills up or you call `flush()` (or `close()`). If the
program ends without flushing, the last part of the output, possibly *all* of it, is never
written, and the judge reports "wrong answer" for a correct program. Put `out.flush()` at
the end of `main`, and make sure no `return` statement skips it.
:::

### Formatting numbers, and the locale trap

`System.out.printf` and `String.format` format numbers: `%d` for integers, `%.2f` for a
`double` with two decimal places, `%,d` with thousands separators, and `%n` for a line
break. These methods follow a *locale*: a setting that describes a language and region's
conventions for writing numbers and dates. By default they use the locale of the computer
the program runs on.

```{.java include="code-examples/ch02/LocaleTrap.java" region="formats"}
```

```{.output from="code-examples/ch02/LocaleTrap"}
```

The same format prints **Bengali digits** under the Bangladeshi locale `bn-BD`, and a
decimal *comma* under the German locale. On a judge, the default locale is usually
US English, so this rarely fails there. On your own computer, if Windows is set to Bangla
(Bangladesh), your local tests can print `৩.৫০` where the expected output says `3.50`.
Pass `Locale.ROOT` (a neutral locale) whenever you format numbers for a judge, a file or
another program: `String.format(Locale.ROOT, "%.2f", x)`.

`StringBuilder.append(double)` and `String.valueOf(double)` do not depend on the locale,
but they print values such as `0.30000000000000004` or `1.0E-4` instead of a fixed
number of decimals. When a problem says "print the answer with exactly 6 digits after the
decimal point", use `String.format(Locale.ROOT, "%.6f", value)`.

## Input patterns {#input-patterns}

### Multiple test cases {#multiple-tests}

Many problems begin with a number T, followed by T independent test cases. The
template's `main` method reads T and calls `solve` once per test case:

```{.java include="code-examples/ch02/OaTemplate.java" region="main" kind="Method"}
```

```{.java include="code-examples/ch02/OaTemplate.java" region="solve" kind="Method"}
```

```{.input from="code-examples/ch02/OaTemplate"}
```

```{.output from="code-examples/ch02/OaTemplate"}
```

Keeping each test case in its own `solve` call has a practical benefit: every variable is
fresh for each test, so nothing leaks from one test into the next. If you keep arrays or
counters in `static` fields to reuse memory, reset them at the start of `solve`. The
complete template, including its reader `FastIn`, is explained in []{.ref to="templates"}.

### Reading until the end of the input {#until-eof}

Some inputs do not say how many lines follow. Read until `readLine()` returns `null`:

```{.java include="code-examples/ch02/ReadUntilEof.java" #lst-eof}
```

```{.input from="code-examples/ch02/ReadUntilEof"}
```

```{.output from="code-examples/ch02/ReadUntilEof"}
```

The condition on line 15 does two things at once: `(line = reader.readLine())` assigns
the next line to `line`, and the value of the whole assignment expression is then compared
with `null`. Blank lines are skipped (lines 17–19), which makes the program robust against
an extra empty line at the end of the input, a common difference between local test files
and judge input. When typing input by hand in a terminal, end it with Ctrl+Z then Enter on
Windows, or Ctrl+D on Linux and macOS.

### Reading a grid {#grid-input}

Grid problems give the size and then one line per row:

```{.java include="code-examples/ch02/GridInput.java" #lst-grid}
```

```{.input from="code-examples/ch02/GridInput"}
```

```{.output from="code-examples/ch02/GridInput"}
```

`readLine().toCharArray()` (line 16) turns each row into a `char[]`, so `grid[r][c]` is
the cell in row `r` and column `c`. Remember the order: **row first, then column**, which
is (y, x) rather than (x, y). Most grid bugs come from swapping the two.

### Common input and output mistakes {#io-mistakes}

| Symptom | Likely cause | Fix |
|----------------------------------|------------------------------------------|-------------------------------------------|
| Compilation error on the judge | class not named `Main`, or a `package` line | follow the platform's naming rule; remove `package` |
| "Time limit exceeded" with a fast algorithm | `Scanner`, or `println` per line on large I/O | `BufferedReader` or a fast reader; `PrintWriter` or `StringBuilder` |
| No output at all | `PrintWriter` never flushed | `out.flush()` at the end of `main` |
| `NumberFormatException` | spaces around a number, or reading a whole line as a number | `trim()`, or tokenize first |
| `NullPointerException` from `readLine()` | fewer lines than expected; or the next number is on the same line | read tokens, not lines, with a `next()` helper |
| Wrong answer only on your computer | locale-dependent formatting | `Locale.ROOT` in `String.format` |
| Wrong answer with "correct" numbers | wrong output format: missing case labels, extra spaces, wrong precision | copy the format from the problem's sample output |
| Wrong answer on large tests only | overflow in a sum or product | `long`, cast before multiplying (Chapter 1) |

: Common input/output errors and their fixes

## Big numbers: BigInteger and BigDecimal {#big-numbers}

Chapter 1's table showed when `int`, `long` and `double` are enough. When they are not,
Java offers two classes in `java.math`.

::: {.definition title="BigInteger and BigDecimal"}
[BigInteger]{.idx} represents integers of any size, limited only by memory.
[BigDecimal]{.idx} represents decimal numbers exactly, as an integer value plus a *scale*
(the number of digits after the decimal point). Both are immutable: every operation
returns a new object.
:::

```{.java include="code-examples/ch02/BigNumbers.java" region="biginteger" kind="Fragment of main"}
```

```{.java include="code-examples/ch02/BigNumbers.java" region="bigdecimal" kind="Fragment of main"}
```

```{.output from="code-examples/ch02/BigNumbers"}
```

**What to notice.**

- Arithmetic uses methods, not operators: `add`, `subtract`, `multiply`, `divide`, `mod`,
  `pow`, `modPow`. Because the objects are immutable, you must keep the result:
  `factorial = factorial.multiply(...)` (line 3 of the first fragment). Writing
  `factorial.multiply(...)` alone computes a value and throws it away.
- Compare with `compareTo` (negative, zero or positive), not with `<` or `==`.
- `BigInteger.valueOf(long)` converts small values; `new BigInteger("…")` parses text of
  any length.
- `new BigDecimal("0.1")` is exactly 0.1, but `new BigDecimal(0.1)` is the exact value of
  the *double* closest to 0.1, a long string of digits. Always create `BigDecimal` values
  from strings (or with `BigDecimal.valueOf(double)`, which goes through the shortest
  string representation).
- `BigDecimal.equals` compares the scale too, so `2.0` and `2.00` are *not* equal, while
  `compareTo` says they are the same number. Use `compareTo` to compare values.
- Division that does not terminate, such as 1/3, throws `ArithmeticException` unless you
  give a scale and a `RoundingMode`.

**Cost.** Operations on big numbers take time that grows with the number of digits:
addition is linear in the number of digits, and multiplication is slower than that. They
are also objects, so arrays of them use a lot of memory. In contest problems, a result
"modulo 1,000,000,007" is a strong hint that the intended solution uses `long` with
modular arithmetic (Chapter 19), not `BigInteger`.

## Arrays and Collections utilities {#utilities}

`java.util.Arrays` works on arrays; `java.util.Collections` works on lists and other
collections. These are the methods you will use most often.

```{.java include="code-examples/ch02/LibraryUtilities.java" region="arrays" kind="Fragment of main"}
```

```{.java include="code-examples/ch02/LibraryUtilities.java" region="aslist" kind="Fragment of main"}
```

```{.java include="code-examples/ch02/LibraryUtilities.java" region="collections" kind="Fragment of main"}
```

```{.output from="code-examples/ch02/LibraryUtilities"}
```

**Details that matter.**

- `Arrays.binarySearch` requires a *sorted* array. If the key is present, it returns its
  index (if it appears several times, any one of them). If not, it returns
  `-(insertion point) - 1`, where the insertion point is the index of the first element
  greater than the key. For 35 in `[10, 20, 30, 40, 50]`, the insertion point is 3, so the
  result is −4. The formula guarantees a negative result whenever the key is missing,
  even when the insertion point is 0. Chapter 12 builds binary search from scratch.
- `Arrays.sort(part, 1, 4)` sorts the half-open range from index 1 up to, but not
  including, index 4.
- `Arrays.asList(array)` returns a *fixed-size* list that is a view of the array: setting
  an element writes through to the array (the output shows `plum` in both), and adding or
  removing throws `UnsupportedOperationException`. For a normal list, copy it:
  `new ArrayList<>(Arrays.asList(array))`. `Arrays.asList` on an `int[]` gives a
  `List<int[]>` with one element, not a list of numbers.
- `List.of(...)` returns an *unmodifiable* list, and it rejects `null` elements. It is
  perfect for constants and test data; copy it into an `ArrayList` when you need to
  change it.

| Method | What it does | Cost |
|-----------------------------------------|------------------------------------------|-----------|
| `Arrays.sort(a)`, `Arrays.sort(a, from, to)` | sort ascending, in place | O(n log n) |
| `Arrays.binarySearch(a, key)` | search a sorted array | O(log n) |
| `Arrays.fill(a, value)` | set every element | O(n) |
| `Arrays.copyOf`, `copyOfRange`, `equals`, `toString` | see Chapter 1 | O(n) |
| `Collections.sort(list)`, `list.sort(cmp)` | stable sort of a list | O(n log n) |
| `Collections.reverse`, `max`, `min`, `frequency` | reverse, extremes, count | O(n) |
| `Collections.swap(list, i, j)` | swap two elements | O(1) for `ArrayList` |
| `Collections.nCopies(n, x)` | an unmodifiable list of n copies | O(1) to create |

: Frequently used Arrays and Collections methods

## Lambda and method-reference essentials {#lambdas}

Lambdas appear in DSA code from the first chapter of problems onwards: in every
comparator, in every `computeIfAbsent`, in `PriorityQueue` constructors. This section
teaches what you need now; Chapter 22 covers functional programming and streams in depth.

::: {.definition title="Functional interface and lambda"}
A [functional interface]{.idx} is an interface with exactly one abstract method, such as
`Comparator<T>` (method `compare`) or `Runnable` (method `run`). A
[lambda expression]{.idx} is a short way to create an object that implements a functional
interface: you write only the parameters and the body of that one method.
:::

**Why lambdas exist.** Before Java 8, sorting by length needed an anonymous class with
five lines of ceremony around one line of logic. A lambda keeps only the logic.

```{.java include="code-examples/ch02/LambdaBasics.java" region="forms" kind="Fragment of main"}
```

**Syntax breakdown.**

- `(String a, String b) -> Integer.compare(a.length(), b.length())` (lines 1–2) is the
  full form: a parameter list, the arrow `->`, and an expression whose value is returned.
- `(a, b) -> ...` (lines 3–4) leaves out the parameter types. The compiler infers them
  from the *target type*: the variable is a `Comparator<String>`, whose `compare` method
  takes two `String`s. A lambda has no type of its own; it always takes the type of the
  functional interface it is assigned or passed to.
- `() -> System.out.println(...)` (line 5) has no parameters. With exactly one parameter,
  the parentheses are optional: `x -> x * 2`.
- A body in braces (lines 6–9) can contain several statements and must use `return` to
  return a value.

The variable `add` has type `BinaryOperator<Integer>`, a functional interface from
`java.util.function` that takes two values of a type and returns one of the same type.
`apply` (line 12) calls it.

### Method references

When a lambda only calls one existing method, a [method reference]{.idx} is shorter:

```{.java include="code-examples/ch02/LambdaBasics.java" region="refs" kind="Fragment of main"}
```

| Kind | Example | Equivalent lambda |
|----------------------------------|---------------------------|-----------------------------------------|
| static method | `Integer::compare` | `(x, y) -> Integer.compare(x, y)` |
| instance method of a parameter | `String::length` | `s -> s.length()` |
| instance method of a given object | `System.out::println` | `x -> System.out.println(x)` |
| constructor | `ArrayList::new` | `() -> new ArrayList<>()` |

: The four kinds of method reference

`Comparator.comparingInt(String::length)` (line 2) builds a comparator from a function
that extracts an `int` key: here, the length of each string. `Supplier<List<String>>`
(line 3) is a functional interface whose method `get()` takes nothing and returns a value;
`ArrayList::new` supplies a new list each time.

### Lambdas with maps

Two `Map` methods take lambdas and replace several lines of everyday code:

```{.java include="code-examples/ch02/LambdaBasics.java" region="maps" kind="Fragment of main"}
```

- `counts.merge(word, 1, Integer::sum)` (line 4) means: if `word` is not in the map, put
  1; otherwise replace the old value with `Integer.sum(old, 1)`. It is the shortest
  correct way to count.
- `byLen.computeIfAbsent(key, k -> new ArrayList<>())` (line 8) returns the list stored
  under `key`, first creating and storing an empty one if there is none. Chaining `.add(word)`
  then adds to that list. This is the standard way to build a "map of lists". The lambda
  runs only when the key is missing.

### Capturing local variables

```{.java include="code-examples/ch02/LambdaBasics.java" region="capture" kind="Fragment of main"}
```

A lambda may read local variables of the enclosing method only if they are *effectively
final*: never reassigned after initialization. If line 4 were not a comment, the compiler
would reject the lambda on line 2. The usual workaround in contest code is to copy the
value into a new final variable just before the lambda, or to use a one-element array or a
field when the lambda must update a value.

```{.output from="code-examples/ch02/LambdaBasics"}
```

## Sorting with built-in APIs and comparators {#sorting}

### What Java guarantees

| Call | Algorithm (documentation) | Stable? | Time |
|------------------------------|------------------------------------------|----------------|------------|
| `Arrays.sort(int[])` and other primitives | implementation note: Dual-Pivot Quicksort, O(n log n) on all data sets | not applicable | O(n log n) |
| `Arrays.sort(Object[])`, `Arrays.sort(T[], Comparator)` | any algorithm, but stability is required | yes (guaranteed) | O(n log n) |
| `Collections.sort(list)`, `list.sort(cmp)` | delegates to `List.sort`; stability required | yes (guaranteed) | O(n log n) |

: What the Java SE 21 documentation says about sorting

::: {.definition title="Stable sort"}
A sort is [stable]{.idx term="stable sort"} if elements that compare as equal keep their
original relative order. Stability matters when you sort by one key after another, for
example first by name and then, stably, by score.
:::

For primitive arrays, "stable" has no meaning, because equal numbers are
indistinguishable. Chapter 12 explains the algorithms behind these guarantees.

### Comparators

::: {.definition title="Comparator"}
A [Comparator]{.idx} `<T>` is a functional interface whose method `compare(a, b)` returns
a negative number if `a` should come before `b`, zero if they are equal in this order, and
a positive number if `a` should come after `b`.
:::

```{.java include="code-examples/ch02/SortingWithComparators.java" region="descending" kind="Fragment of main"}
```

**Descending order.** `Collections.reverseOrder()` (line 2) is a comparator that reverses
the natural order, but comparators only work with *objects*, so it needs an `Integer[]`.
For an `int[]`, there is no `Arrays.sort(int[], Comparator)`. Either box the values, which
costs memory and time (Exercise 2.6), or sort ascending and reverse the array in place
(lines 6–11), which is the usual choice.

```{.java include="code-examples/ch02/SortingWithComparators.java" region="intervals" kind="Fragment of main"}
```

**Sorting arrays of arrays.** `int[][] intervals` is an array of `int[]` objects, so it
can be sorted with a comparator. `Comparator.comparingInt((int[] a) -> a[0])` orders by the
first element, and `.thenComparingInt(a -> a[1])` breaks ties with the second. Notice the
explicit type `(int[] a)` in the first lambda: when a method call is chained after it, the
compiler cannot infer the parameter type from the target, so it needs the hint. Without
the chain, `Comparator.comparingInt(a -> a[0])` would compile.

```{.java include="code-examples/ch02/SortingWithComparators.java" region="objects" kind="Fragment of main"}
```

**Sorting objects by several keys.** `Student` is a *record* declared in the same file:
`record Student(String name, int score) { }`. A record is a compact way to declare a
class whose fields never change; it automatically gets a constructor, accessor methods
`name()` and `score()`, and `equals`, `hashCode` and `toString` (Chapter 3).
`Comparator.comparingInt(Student::score).reversed()` sorts by score, highest first, and
`.thenComparing(Student::name)` puts students with equal scores in alphabetical order.

```{.java include="code-examples/ch02/SortingWithComparators.java" region="overflow" kind="Fragment of main"}
```

```{.output from="code-examples/ch02/SortingWithComparators"}
```

::: {.warning title="Never compare by subtraction"}
`(a, b) -> a - b` looks like a neat comparator, and it works in every small test. But
`a - b` overflows when the values are far apart: here `Integer.MIN_VALUE - 1` wraps around
to a large *positive* number, so the comparator claims that the smallest `int` is larger
than 1. The result is the scrambled order in the output, and with longer arrays, Java's
sort may even throw `IllegalArgumentException: Comparison method violates its general
contract!`. Always use `Integer.compare(a, b)`, `Long.compare`, `Comparator.comparingInt`,
or `Comparator.reverseOrder()`.
:::

| Need | Comparator |
|------------------------------------------|----------------------------------------------------------|
| natural order / reversed | `Comparator.naturalOrder()` / `Comparator.reverseOrder()` |
| by an `int` key | `Comparator.comparingInt(x -> key)` |
| by a key, descending | `Comparator.comparingInt(...).reversed()` |
| tie-breaker | `.thenComparing(...)`, `.thenComparingInt(...)` |
| `int[]` by element 0, then element 1 | `Comparator.comparingInt((int[] a) -> a[0]).thenComparingInt(a -> a[1])` |
| `long` values | `Long::compare` or `Comparator.comparingLong(...)` |
| `null` allowed | `Comparator.nullsFirst(...)` or `nullsLast(...)` |

: Comparator recipes

## A reusable template {#templates}

Every online assessment should start from a template you understand completely. Here is
the full template from this chapter. It needs only Java 8 features, so it compiles on any
judge that offers Java 8 or later.

```{.java include="code-examples/ch02/OaTemplate.java" #lst-template}
```

**How the parts fit.**

- **Lines 1–4: imports.** Only the classes the template uses. Many contestants write
  `import java.io.*;` and `import java.util.*;` instead, which is shorter and equally
  correct.
- **The class name.** Most judges require the class that contains `main` to be called
  `Main` and to be in no package. Rename `OaTemplate` before you submit; LeetCode instead
  gives you a `Solution` class and calls your method directly, without any input reading.
- **`main` (lines 13–21).** Creates the reader and writer, reads the number of test cases,
  calls `solve` for each, and flushes once at the end.
- **`solve` (lines 23–35).** The only part you replace for a new problem.
- **`FastIn` (lines 37–93).** A compact version of `FastReader`: `read()` returns the next
  byte, or −1 at the end of the input, refilling the 64 KB buffer when it is empty;
  `nextLong()` skips whitespace, reads an optional minus sign and then digits; `next()`
  reads a word. It is a `static` nested class so that `main` can create it without an
  instance of the outer class (Chapter 3).

To adapt the template to a problem without test cases, replace the loop in `main` by a
single `solve(in, out)` call. To read a grid, call `next()` once per row and use
`toCharArray()`. If you prefer `BufferedReader`, the `next()` helper of Exercise 2.1 gives
you the same token-based reading.

::: {.tip title="Practise with your template before the assessment"}
Type your template from memory a few times, and submit it to an easy problem on the
platform you will be tested on. During a timed assessment you want to spend your
attention on the problem, not on remembering how to flush a `PrintWriter`.
:::

## Summary {#summary}

For large inputs, `Scanner` is too slow; use `BufferedReader` with `StringTokenizer`, or a
byte-level fast reader. Build output in a `StringBuilder` or a buffered `PrintWriter`, and
always flush. Format numbers with `Locale.ROOT`, because the default locale can change
digits and decimal separators. Handle the standard input shapes: T test cases, reading
until `readLine()` returns `null`, and grids read row by row. Use `long` before reaching
for `BigInteger`, and create `BigDecimal` values from strings. `Arrays` and `Collections`
provide sorting, searching and other utilities; know which lists are fixed-size or
unmodifiable. Lambdas and method references implement functional interfaces concisely;
`merge` and `computeIfAbsent` make counting and grouping one line each. Build comparators
with `Integer.compare`, `comparingInt`, `thenComparing` and `reversed`, never with
subtraction.

| Topic | Remember |
|-------------------------|----------------------------------------------------------|
| Fast input | `BufferedReader` + `StringTokenizer`, or a byte-level reader |
| Fast output | `StringBuilder` or `PrintWriter(new BufferedOutputStream(System.out))` |
| Flush | `out.flush()` at the end of `main`, or the output is lost |
| Locale | `String.format(Locale.ROOT, "%.2f", x)` |
| Until EOF | `while ((line = reader.readLine()) != null)` |
| Big numbers | `BigInteger` for huge integers; `BigDecimal` from strings; `compareTo` |
| `binarySearch` | index if found, otherwise `-(insertion point) - 1` |
| `Arrays.asList` / `List.of` | fixed-size view / unmodifiable |
| Lambdas | implement a functional interface; capture only effectively final locals |
| Counting, grouping | `merge(key, 1, Integer::sum)`, `computeIfAbsent(key, k -> new ArrayList<>())` |
| Comparators | `Integer.compare`, `comparingInt`, `thenComparing`, `reversed`; never `a - b` |
| Template | read T, `solve` per test, flush once; rename the class to `Main` |

: Key takeaways

## Exercises {#exercises}

::: {.exercise number="2.1" level="Easy" title="Tokens across lines"}
`BufferedSum` assumes that all n numbers are on one line. Write a `next()` helper,
based on `BufferedReader` and `StringTokenizer`, that returns the next token wherever it
is: on the current line, on a later line, or after blank lines. It should return `null`
at the end of the input. Use it to sum n numbers spread over several lines.
:::

::: {.exercise number="2.2" level="Easy" title="Count lines, words and characters"}
Read the whole input until its end and print the number of lines, the number of words
(maximal runs of non-whitespace characters) and the number of characters, not counting
line breaks. The input may contain blank lines and lines that start with spaces.
:::

::: {.exercise number="2.3" level="Easy" title="By length, then alphabetically"}
Sort an array of words by length, shortest first; words of equal length should be in
alphabetical order. Write the comparator with `Comparator` methods, not with a hand-written
`if` chain.
:::

::: {.exercise number="2.4" level="Medium" title="Break the subtraction comparator"}
Find two `Integer` values for which `(a, b) -> a - b` gives the wrong sign, and show an
array that it sorts incorrectly. Then fix the comparator. Under what condition on the
values would subtraction be safe?
:::

::: {.exercise number="2.5" level="Medium" title="Sort intervals"}
Sort an `int[][]` of intervals `{start, end}` by start in ascending order, and for equal
starts by end in *descending* order (the longest interval first). This order is used by
several interval algorithms in Chapter 21.
:::

::: {.exercise number="2.6" level="Medium" title="Descending primitives"}
Write two methods that sort an `int[]` in descending order: one that boxes the values into
an `Integer[]` and uses `Collections.reverseOrder()`, and one that never boxes. Compare
them in memory use and speed, and explain the difference.
:::

::: {.exercise number="2.7" level="Medium" title="Group by first letter"}
Given an array of words, return a map from each first letter to the list of words that
start with it, in their original order, with the letters in alphabetical order. Skip empty
words. Use `computeIfAbsent`.
:::

::: {.exercise number="2.8" level="Easy" title="Word frequencies"}
Count how often each word occurs in a text, ignoring case and punctuation, and return the
words with their counts, most frequent first; words with equal counts should be in
alphabetical order. Use `Map.merge` to count.
:::

::: {.exercise number="2.9" level="Medium" title="Beyond long"}
Use `BigInteger` to compute the sum of the digits of 100! and the number of decimal digits
of 2^1000^. Could either answer be computed with `long` arithmetic alone? Explain.
:::

::: {.exercise number="2.10" level="Medium" title="Exact money"}
A shop sells items at 19.99, 5.01 and 0.10, and a customer buys 3, 2 and 1 of them.
Compute the total exactly with `BigDecimal`, rounded to two decimal places, and compare
with the same computation in `double`. Then explain why `new BigDecimal("70.09")` and
`new BigDecimal("70.090")` are not `equals` but `compareTo` returns 0.
:::

::: {.exercise number="2.11" level="Hard" title="Distinct values per test case"}
The input has T test cases. Each has n followed by n integers; the total of all n is at
most 2 × 10^5^. For each test case, print `Case #t:` followed by every distinct value in
increasing order, each written as `value` `x` `count` (for example `4x3`). Make the input and
output fast, and explain the time complexity.
:::

::: {.exercise number="2.12" level="Challenge" title="Parse a line by hand"}
Write `int[] parseInts(String line)` that returns all integers on a line, in order,
without using `split`, `StringTokenizer` or `Scanner`. Numbers may be negative and may be
separated by any number of spaces or tabs. Use it to print the sum of every line of the
input until its end.
:::

## Solutions {#solutions}

### Warm-up quiz answers {#quiz-answers}

1. `BufferedReader` (with `StringTokenizer` or manual parsing). `Scanner` matches the
   input against regular expressions for every token, which costs far more than reading
   characters from a buffer. ([]{.ref to="reading-input"})
2. `out.flush()` (or `close()`) at the end of `main`. ([]{.ref to="fast-output"})
3. `a - b` overflows when the values are far apart, giving the wrong sign.
   ([]{.ref to="sorting"})
4. `-(insertion point) - 1`, which is always negative. ([]{.ref to="utilities"})
5. Nothing that matters: both create a `Comparator` that calls `Integer.compare`; the
   method reference is just shorter. ([]{.ref to="lambdas"})

::: {.solution number="2.1"}
Keep one `StringTokenizer` and replace it with the next line's tokenizer whenever it has
no more tokens. Blank lines produce an empty tokenizer, so the loop simply reads on.

```{.java include="solutions/ch02/TokenReaderSum.java" region="solve" kind="Fields and methods"}
```

```{.input from="solutions/ch02/TokenReaderSum"}
```

```{.output from="solutions/ch02/TokenReaderSum"}
```

Each line is read once and each token is produced once, so reading takes time
proportional to the input size. Returning `null` at the end of the input lets a caller
write `while ((token = next()) != null)` to read until the end.
:::

::: {.solution number="2.2"}
```{.java include="solutions/ch02/WordCount.java" region="solve" kind="Fragment of main"}
```

```{.input from="solutions/ch02/WordCount"}
```

```{.output from="solutions/ch02/WordCount"}
```

`readLine()` returns lines without their line breaks, so `line.length()` counts exactly
the characters on the line. `StringTokenizer.countTokens()` counts the words on a line
without creating them, and a blank line simply contributes zero words. The input has four
lines, one of them blank.
:::

::: {.solution number="2.3"}
```{.java include="solutions/ch02/SortByLength.java" region="solve"}
```

```{.output from="solutions/ch02/SortByLength"}
```

`Comparator.comparingInt(String::length)` compares the lengths, and
`thenComparing(Comparator.naturalOrder())` compares equal-length words alphabetically.
The sort is O(n log n) comparisons, and each comparison costs O(length of the words)
in the worst case.
:::

::: {.solution number="2.4"}
```{.java include="solutions/ch02/ComparatorBug.java" region="solve" kind="Fields"}
```

```{.output from="solutions/ch02/ComparatorBug"}
```

For `low = -2,000,000,000` and `high = 2,000,000,000`, the true difference −4 × 10^9^ does
not fit in an `int` and wraps around to the positive value 294,967,296, so the comparator
says `low > high`, and the two-element array stays in the wrong order. Subtraction is
safe only when every possible difference fits in an `int`, for example when all values
are known to be between −2^30^ and 2^30^. Rather than reasoning about that each time, use
`Integer::compare`, which is always correct.
:::

::: {.solution number="2.5"}
```{.java include="solutions/ch02/SortIntervals.java" region="solve"}
```

```{.output from="solutions/ch02/SortIntervals"}
```

When the starts differ, compare starts; otherwise compare the ends with the arguments
*swapped* (`b[1]`, `a[1]`), which reverses that order. The same comparator can be built
from parts: `Comparator.comparingInt((int[] a) -> a[0]).thenComparing((int[] a) -> a[1],
Comparator.reverseOrder())`, but the explicit version is easier to check.
:::

::: {.solution number="2.6"}
```{.java include="solutions/ch02/SortDescending.java" region="solve" kind="Methods"}
```

```{.output from="solutions/ch02/SortDescending"}
```

The boxed version creates an `Integer` object for every value outside the cached range
−128 to 127, and each object takes several times the 4 bytes of an `int` (Chapter 5),
plus a reference in the `Integer[]`. It also sorts with a comparator, which is slower than
sorting primitives. The in-place version uses no extra memory beyond what `Arrays.sort`
needs internally, and the reversal is O(n). Both are O(n log n) overall; the in-place
version is the better choice.
:::

::: {.solution number="2.7"}
```{.java include="solutions/ch02/GroupByFirstLetter.java" region="solve"}
```

```{.output from="solutions/ch02/GroupByFirstLetter"}
```

A `TreeMap` keeps its keys sorted (Chapter 7), so the letters come out in alphabetical
order; an `ArrayList` keeps the words in their original order. `computeIfAbsent` creates
each list exactly once, the first time its letter appears. The empty string is skipped,
because `charAt(0)` would throw on it.
:::

::: {.solution number="2.8"}
```{.java include="solutions/ch02/WordFrequencies.java" region="solve"}
```

```{.output from="solutions/ch02/WordFrequencies"}
```

`split("\\W+")` splits on runs of characters that are not letters, digits or underscores,
which removes punctuation. The map entries are copied into a list and sorted with
`Map.Entry.comparingByValue().reversed()` followed by `comparingByKey()`. The explicit
type arguments `<String, Integer>` are needed for the same reason as the explicit
`(int[] a)` in the interval comparator: the call is the start of a chain.
:::

::: {.solution number="2.9"}
```{.java include="solutions/ch02/BigDigits.java" region="solve" kind="Methods"}
```

```{.output from="solutions/ch02/BigDigits"}
```

100! has 158 digits and 2^1000^ has 302, far beyond the 19 digits of a `long`, so neither
number can be computed in `long`. The *number of digits* of 2^1000^ can be found without
big numbers, though: it is ⌊1000 × log~10~ 2⌋ + 1 = 302, using `Math.log10`. The digit sum
of 100! has no such shortcut.
:::

::: {.solution number="2.10"}
```{.java include="solutions/ch02/PriceTotal.java" region="solve"}
```

```{.output from="solutions/ch02/PriceTotal"}
```

The `BigDecimal` total is exactly 70.09; the `double` total is 70.08999999999999,
because none of 19.99, 5.01 and 0.10 is exactly representable in binary. `equals`
compares both the value and the scale, and 70.09 has scale 2 while 70.090 has scale 3, so
they are not equal; `compareTo` compares only the numeric value. For money, store amounts
as `BigDecimal` from strings (or as whole paisa in a `long`), and compare them with
`compareTo`.
:::

::: {.solution number="2.11"}
Sort each test case; equal values become neighbours, so one pass groups them.

```{.java include="solutions/ch02/DistinctCounts.java" region="solve" kind="Method"}
```

```{.input from="solutions/ch02/DistinctCounts"}
```

```{.output from="solutions/ch02/DistinctCounts"}
```

Sorting costs O(n log n) per test case, and the grouping pass is O(n), because the inner
loop's index `j` only moves forward and the outer loop restarts from it. Over all test
cases the time is O(N log N) for N total numbers. Building each line in a
`StringBuilder` and printing it through a buffered `PrintWriter` keeps output fast even
when every value is distinct. (A `TreeMap` from value to count would also work, in
O(N log N) time, but with much more memory.)
:::

::: {.solution number="2.12"}
```{.java include="solutions/ch02/LineParser.java" region="solve"}
```

```{.input from="solutions/ch02/LineParser"}
```

```{.output from="solutions/ch02/LineParser"}
```

The parser walks the line once: spaces and tabs are skipped, an optional minus sign is
consumed, and digits are accumulated as in the fast reader. A line of length L contains at
most L/2 + 1 numbers, since every number needs at least one digit and numbers are separated
by at least one space, so that array size is always enough; `Arrays.copyOf` trims it at
the end. Each line costs O(L), and the empty third line correctly gives an empty array.
:::
