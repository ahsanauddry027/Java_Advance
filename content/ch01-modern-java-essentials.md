# Modern Java Essentials for DSA {#modern-java-essentials}

::: objectives
**After this chapter you will be able to:**

- explain what happens between writing a `.java` file and seeing its output, and the
  roles of the JDK, the compiler, bytecode and the JVM;
- choose a Java version for practice and online assessments, and know which features
  belong to which version;
- predict exactly what Java does with primitive and reference variables, including when a
  method can and cannot change its caller's data;
- avoid the numeric traps that cause most "wrong answer" verdicts: overflow, integer
  division, floating-point error and character arithmetic;
- use `==` and `equals` correctly, and explain the string pool and the `Integer` cache;
- build strings efficiently and choose between `String`, `StringBuilder` and
  `StringBuffer`;
- use `final`, `static`, `this` and `super` with intent, handle `null` safely, and copy
  one- and two-dimensional arrays correctly.

**Prerequisites:** the Java fundamentals listed in the Preface. Nothing from earlier
chapters is needed.
:::

::: {.quiz title="Diagnostic"}
Answer these before you read the chapter. The answers, and the section to read if you
missed one, are at the start of the Solutions section.

1. What does `javac` produce, and what program runs it?
2. What does `System.out.println(7 / 2 + 7 % 2);` print?
3. Can an `int` hold 3,000,000,000? If not, which type would you use?
4. What value does `long big = 50_000 * 50_000;` store?
5. After `int[] a = {1, 2}; int[] b = a; b[0] = 9;`, what is `a[0]`?
6. Is Java "pass by value" or "pass by reference"?
7. What is `"abc" == new String("abc")`, and why?
8. Why is `s += x` inside a long loop slow when `s` is a `String`?
9. With `Integer a = 1000, b = 1000;`, what is `a == b`?
10. What are the elements of `new boolean[3]` and of `new String[3]`?
11. `counts` is a `Map<String, Integer>`. What happens on `int n = counts.get("missing");`?
12. What is the difference between `StringBuilder` and `StringBuffer`?
:::

You already write Java. This chapter makes sure you also know *exactly* what your Java
does, because in problem solving the small details decide the verdict. A solution with the
right idea still fails if a product overflows an `int`, if two strings are compared with
`==`, or if a loop builds a long string one `+=` at a time. Each section below takes one
of these details, explains what the language guarantees, shows a program that was run
while this book was built, and points out the traps.

## How Java runs your code {#how-java-runs}

::: {.definition title="Bytecode and the JVM"}
[Bytecode]{.idx} is the instruction set of an imaginary computer, stored in `.class`
files. The [Java Virtual Machine]{.idx term="JVM (Java Virtual Machine)"} (JVM) is the
program that loads `.class` files and executes their bytecode on a real computer.
:::

Running a Java program takes two steps:

```{.diagram caption="From source code to a running program"}
  Hello.java  --javac-->  Hello.class  --java-->  JVM: loads, verifies, runs
  (source)    compiler    (bytecode)    launcher   interprets bytecode at first,
                                                   then compiles hot methods to
                                                   machine code (JIT)
```

1. **Compile.** `javac` (the Java compiler) checks your source code for errors and
   translates each class into a `.class` file containing bytecode.
2. **Run.** `java` starts a JVM, which loads the classes it needs, *verifies* that the
   bytecode is safe, and executes it. At first the JVM *interprets* bytecode one
   instruction at a time. Methods that run often ("hot" methods) are then compiled to
   native machine code by the [just-in-time compiler]{.idx term="JIT compiler"} (JIT).

**Why two steps?** Bytecode is the same on every operating system, so a `.class` file
compiled on Windows runs on any JVM, on Linux or macOS too. The JIT then gives speed
close to that of natively compiled languages for code that runs long enough to be
optimized. Chapter 5 looks inside the JVM in detail.

The program below lets you see real bytecode. It asks `javap`, the JDK's class-file
disassembler, to describe the compiled `add` method of the program itself.

```{.java include="code-examples/ch01/ShowBytecode.java" #lst-bytecode}
```

```{.output from="code-examples/ch01/ShowBytecode"}
```

`iload_0` and `iload_1` push the two `int` parameters onto the method's *operand
stack*, `iadd` pops both and pushes their sum, and `ireturn` returns the top value. The
JVM is a *stack machine*: almost every instruction takes its inputs from, and leaves its
result on, a stack (Chapter 10 builds stacks of its own).

**How the program works.** Line 13 looks up the `javap` tool through
`java.util.spi.ToolProvider`, which lets a Java program run JDK tools in the same
process. `findFirst` returns an `Optional<ToolProvider>` (a box that may be empty;
Chapter 22) and `orElseThrow()` takes the tool out, or throws if this runtime has no
`javap`. Lines 16–17 run it with the options `-c` (show bytecode) and `-cp` (where to
find classes), writing into a `StringWriter`. Lines 19–29 print only the lines about
`add`. `split("\\R")` splits the text at any kind of line break.

**JDK, JRE and JVM.** The terms are often confused:

| Term | What it is | You need it to… |
|------|------------------------------------------------|-------------------|
| JVM | the virtual machine that runs bytecode | run any Java program |
| JRE | *Java Runtime Environment*: a JVM plus the standard class library | run programs |
| JDK | *Java Development Kit*: a JRE plus development tools (`javac`, `javap`, `jshell`, `jar`) | write and compile programs |

: JVM, JRE and JDK

Since Java 11, the JDK is the normal download for everyone; some vendors still publish
runtime-only packages. Install a JDK.

::: {.note title="Quick experiments"}
Since Java 11 you can run a single source file without compiling it first:
`java Hello.java`. The launcher compiles the file in memory and runs it. `jshell`, the
interactive Java shell that ships with the JDK, is even quicker for checking one
expression, such as `-7 % 2` or `(int) 3.99`.
:::

## Java versions and LTS releases {#java-versions}

Since Java 10 (March 2018), a new Java version has been released every six months, in
March and September. Most versions are supported only until the next one appears. Some
are **long-term support** ([LTS]{.idx term="LTS (long-term support)"}) releases, which
vendors maintain for years; companies build and run their software on LTS versions.

| LTS version | Released | Highlights you will use in this book |
|-------------|----------------|------------------------------------------------------|
| Java 8 | March 2014 | lambdas, method references, streams |
| Java 11 | September 2018 | `var` (from 10), `String.strip`, `isBlank`, `repeat`, running `java File.java` |
| Java 17 | September 2021 | switch expressions (14), text blocks (15), records and `instanceof` patterns (16), sealed classes |
| Java 21 | September 2023 | sequenced collections, pattern matching for `switch`, record patterns, virtual threads |
| Java 25 | September 2025 | compact source files and instance `main` methods, module import declarations, flexible constructor bodies, scoped values |

: Long-term support releases and the features introduced up to each one

This book uses **Java 21** as its baseline because it is a widely available LTS release
and has every language feature that matters for problem solving. Java 25 is newer; its
features are marked "Java 25" wherever they appear. For example, Java 25 finalized
*compact source files*, which let a small program skip the class declaration:

```{.java include="code-examples/ch01/java25/HelloCompact.java" kind="Complete program (Java 25)"}
```

```{.output from="code-examples/ch01/java25/HelloCompact"}
```

`IO.println` is a Java 25 convenience class for console output. The same file does not
compile with `javac --release 21`; the compiler reports that implicitly declared classes
are not supported in that version. That is exactly why online-assessment code should stay
with features your judge supports.

**What do online judges run?** It varies, and it changes. When this book was written,
HackerRank's official *Execution Environment* page listed separate Java 8, 15, 17 and 21
options (Java 21 as OpenJDK 21.0.4), plus an option labelled simply "Java" that runs Java
1.7 (page checked on 2026-10-15). LeetCode and Codeforces do not offer a page that could be
checked automatically, so their versions are not stated here. Always look at the language
menu of the platform you use.

::: {.warning title="Pick the version explicitly"}
On a platform that offers several Java versions, choose the newest one you know works. A
solution that uses `var`, `List.of` or switch expressions fails to *compile* on Java 7 or
8, and a compile error usually scores zero. The templates in this book avoid features newer
than Java 17 unless they say otherwise.
:::

## Primitive and reference types {#primitive-vs-reference}

Java has two kinds of types, and they behave differently in assignments, comparisons and
method calls.

::: {.definition title="Primitive and reference types"}
A variable of a [primitive type]{.idx} (`int`, `long`, `double`, `char`, `boolean`,
`byte`, `short`, `float`) holds the value itself. A variable of a
[reference type]{.idx} (every class, interface, array and enum) holds a *reference*: the
information the JVM needs to find an object, or `null` if it refers to nothing.
:::

**Why both exist.** Primitives are small and fast: an `int` is 4 bytes, and an `int[]`
of a million elements is one block of 4 MB. Objects carry extra bookkeeping (Chapter 5),
but they can represent anything and can be shared between different parts of a program.

| Type | Size | Range | Default | Typical DSA use |
|---------|------------------|-------------------------------------------|---------|------------------|
| `byte` | 8 bits | −128 to 127 | `0` | compact flags |
| `short` | 16 bits | −32,768 to 32,767 | `0` | rarely used |
| `int` | 32 bits | −2^31^ to 2^31^ − 1 (about ±2.1 × 10^9^) | `0` | indices, counts, most values |
| `long` | 64 bits | −2^63^ to 2^63^ − 1 (about ±9.2 × 10^18^) | `0L` | sums, products, timestamps |
| `float` | 32 bits | about 7 significant decimal digits | `0.0f` | avoid in DSA |
| `double` | 64 bits | about 15–16 significant decimal digits | `0.0` | geometry, probabilities |
| `char` | 16 bits | 0 to 65,535 (one UTF-16 code unit) | `'\u0000'` | characters, letter counts |
| `boolean` | not specified | `true`, `false` | `false` | visited flags |

: The eight primitive types

The defaults apply to fields and array elements. Local variables have no default: the
compiler refuses to read one before it is assigned.

```{.java include="code-examples/ch01/ValuesAndReferences.java" #lst-values-refs}
```

```{.output from="code-examples/ch01/ValuesAndReferences"}
```

**Line by line.** Line 12 copies the *value* 5 into `b`, so incrementing `b` cannot
affect `a`. Line 17 copies the *reference* stored in `first`, so `first` and `second`
now name the same array, and the change on line 18 is visible through both names. Lines
21–25 call `tryToChange`. Inside it, line 5 changes the method's own copy of `number`,
line 6 follows the copied reference to the caller's array and changes it, and line 7
points the method's copy of the reference at a brand-new array, which the caller never
sees.

```{.diagram caption="Two references to one array after int[] second = first"}
   stack frame of main                 heap
   +---------+-----------+
   | a       | 5         |
   | b       | 6         |            +-----+---+---+
   | first   | ref ------+----------->| 100 | 2 | 3 |   one int[] object
   | second  | ref ------+----------->+-----+---+---+
   +---------+-----------+
```

The diagram is a conceptual model: the JVM may store things differently, but every JVM
behaves *as if* it worked like this.

::: {.tip title="\"Is Java pass by reference?\""}
A precise answer: "Java is always pass by value. For an object or array, the value that is
copied is the reference. So a method can change the object the caller's reference points
to, but it cannot make the caller's variable point to a different object." Then show the
three lines of `tryToChange`.
:::

## Numbers: conversions, overflow and precision {#numeric-traps}

More online-assessment submissions fail because of arithmetic details than because of
wrong algorithms. The program below shows the details that matter most; each group of
lines is explained after the output.

```{.java include="code-examples/ch01/NumericTraps.java" #lst-numeric}
```

```{.output from="code-examples/ch01/NumericTraps"}
```

### Overflow {#overflow}

::: {.definition title="Integer overflow"}
[Integer overflow]{.idx} happens when the exact result of an `int` or `long` operation
does not fit in the type. Java does not report it: the result silently *wraps around*,
keeping only the lowest 32 (or 64) bits.
:::

Line 6 adds 1 to the largest `int` and gets the *smallest* `int`, −2,147,483,648. Chapter
19 explains why, using the binary representation of negative numbers (two's complement).
Line 7 writes `1L`, a `long` literal, so Java converts `big` to `long` before adding and
the sum is correct. `Math.addExact` (line 10) is the checked version: it throws
`ArithmeticException` instead of wrapping. `Math` also has `subtractExact`,
`multiplyExact`, `incrementExact`, `negateExact` and `toIntExact`.

**Widening happens too late (lines 15–20).** This is the most common overflow bug in
interviews. In `long wrong = a * b;`, both operands are `int`, so Java multiplies them as
`int` values. The product 10^10^ overflows and wraps to 1,410,065,408, and only *then* is
that wrong value converted to `long`. The variable's type on the left does not influence
how the right side is computed. Casting one operand first, `(long) a * b`, makes the
multiplication itself use `long`.

::: {.warning title="Check the constraints for overflow"}
If a problem says values go up to 10^9^, any sum of two values, and any product, may
overflow `int`. If n values up to 10^9^ are added, the total can reach n × 10^9^. Use
`long` for sums and products, and cast *before* multiplying. Chapter 19 covers modular
arithmetic for answers that do not fit even in a `long`.
:::

### Conversions between numeric types {#conversions}

Java converts automatically in the "safe" direction, from a smaller type to a larger one:
`byte` → `short` → `int` → `long` → `float` → `double`, and `char` → `int`. This is
called *widening*. The other direction, *narrowing*, needs an explicit cast such as
`(int) 3.99`, because information can be lost.

Two rules explain most surprises:

- **Arithmetic promotes small types to `int`.** In `'a' + 1`, the `char` becomes the
  `int` 97, so the result is the `int` 98 (line 40). To get a character back, cast:
  `(char) ('a' + 1)` is `'b'` (line 41). The same rule makes `byteValue + 1` an `int`.
- **Mixed arithmetic uses the larger type.** In `7 / 2.0`, the `int` 7 becomes a
  `double`, so the division is a `double` division: 3.5 (line 26).

Character arithmetic is used constantly in string problems. Because the letters `'a'` to
`'z'` and the digits `'0'` to `'9'` have consecutive codes, `ch - 'a'` turns a lowercase
letter into an index from 0 to 25, and `ch - '0'` turns a digit character into its value
(lines 39 and 42).

**Casting a `double` to an `int`** removes the fractional part, rounding *toward zero*:
3.99 becomes 3 and −3.99 becomes −3 (lines 31–32). If the value is too large for an
`int`, the cast does not wrap; it gives the nearest `int`, here 2,147,483,647 for 10^10^
(line 35). `Math.round` rounds to the nearest whole number, with halves rounded *up*
(toward positive infinity): 2.5 becomes 3 but −2.5 becomes −2 (lines 33–34).

### Integer division and remainder {#division}

Integer division discards the fraction, rounding toward zero: `7 / 2` is 3 and `-7 / 2`
is −3 (lines 22–23). The remainder operator `%` follows the same rule, so `a % b` has the
sign of `a`: `-7 % 2` is −1, not 1. This breaks code like `if (x % 2 == 1)` for negative
odd numbers. Use `x % 2 != 0` to test for odd numbers, or `Math.floorMod(x, 2)`, which
always returns a result between 0 and `b - 1` for positive `b` (lines 24–25).
`Math.floorDiv` rounds toward negative infinity instead.

Division by zero behaves differently for the two kinds of number. `5 / 0` with `int`
operands throws `ArithmeticException`. With `double` operands, `1.0 / 0` is `Infinity`
and `0.0 / 0` is `NaN` ("not a number"), and no exception is thrown (line 36). `NaN` is
not equal to anything, even itself, so test it with `Double.isNaN`.

### Floating-point precision {#floating-point}

A `double` stores numbers in binary, and most decimal fractions, such as 0.1, have no
exact binary representation. The stored value is the closest one available, so small
errors appear: `0.1 + 0.2` is `0.30000000000000004`, and comparing it with `0.3` using
`==` gives `false` (line 29).

Compare doubles with a tolerance instead (line 30): `Math.abs(x - y) < 1e-9`. Choose the
tolerance from the problem; many geometry problems state the precision they require.
Better still, avoid `double` when the problem can be solved with integers: compare `a / b`
with `c / d` as `a * d` with `c * b` (using `long`), and store money as a whole number of
the smallest unit (paisa or cents), or use `BigDecimal` (Chapter 2).

### The midpoint bug {#midpoint}

Binary search computes the middle of a range. The obvious formula `(low + high) / 2`
overflows when `low + high` exceeds the `int` range, which happens on large indices or
when searching over a range of values (Chapter 12). Lines 44–47 show it: the result is a
*negative* index. The safe formula is `low + (high - low) / 2`, because `high - low`
cannot overflow when both are non-negative and `low <= high`.

| Value range | Use | Notes |
|------------------------------------|------------------------|---------------------------------|
| indices, counts, values up to about 2 × 10^9^ | `int` | the default; fastest and smallest |
| sums or products that may pass 2 × 10^9^ | `long` | cast *before* multiplying |
| results larger than 9.2 × 10^18^ | `BigInteger` or modular arithmetic | `BigInteger` is slow; see Chapters 2 and 19 |
| real numbers, ratios, geometry | `double` | compare with a tolerance; never use `float` |
| money, exact decimals | `long` (smallest unit) or `BigDecimal` | never `double` |

: Choosing a numeric type

## Equality: == versus equals {#equality}

::: {.keyidea title="What == compares"}
For primitives, `==` compares *values*. For references, `==` compares *identity*: whether
the two references point to the very same object. To compare the *contents* of two
objects, call `equals`, which each class defines for itself.
:::

```{.java include="code-examples/ch01/EqualityDemo.java" #lst-equality}
```

```{.output from="code-examples/ch01/EqualityDemo"}
```

**Strings (lines 7–19).** Two string literals with the same text are the *same object*
(line 13 prints `true`), because the JVM keeps one shared copy of every literal in the
[string pool]{.idx}. The Java Language Specification guarantees this, and also that a
*constant expression* such as `"ja" + "va"` is pooled (line 16), since the compiler
computes it before the program runs. But `new String("java")` always creates a new object
(line 14), and so does concatenation at run time (line 17). `intern()` returns the pooled
copy of a string's text, which is why line 19 prints `true`. In everyday code you never
need `intern`; you need `equals`, which compares the characters (line 15).

**Boxed integers (lines 21–27).** `Integer small1 = 127;` is compiled to
`Integer.valueOf(127)`. The documentation of `Integer.valueOf` promises that values from
−128 to 127 are cached, so the same object is returned each time, and `==` happens to give
`true`. For 1000 there is no such promise. With default JVM settings a new object is
created each time, and `==` gives `false`. Code that compares `Integer` objects with `==`
therefore passes small tests and fails large ones; Chapter 5 shows where this bites in
collections.

**Arrays (lines 29–33).** Arrays do not override `equals`, so `x.equals(y)` is the same
as `x == y`: identity. Use `Arrays.equals` to compare contents, and `Arrays.deepEquals`
for arrays of arrays.

| To compare… | Use |
|---------------------------------------------|----------------------------------|
| primitives (`int`, `char`, `double`, …) | `==` (for `double`, a tolerance) |
| any object's contents (`String`, `Integer`, …) | `a.equals(b)` |
| objects that might be `null` | `Objects.equals(a, b)` |
| a string with a constant | `"constant".equals(s)` (safe when `s` is `null`) |
| array contents | `Arrays.equals(a, b)`, `Arrays.deepEquals` for 2D |
| enum constants, or whether something is `null` | `==` |

: Choosing between == and equals

How `equals` must be written for your own classes, and why `hashCode` must always agree
with it, is the subject of Chapter 3.

## Strings, StringBuilder and StringBuffer {#strings}

::: {.definition title="Immutable"}
An object is [immutable]{.idx} if its state can never change after it is created. A
`String` is immutable: every method that seems to modify a string, such as `toUpperCase`,
`replace` or `concat`, returns a *new* string and leaves the original untouched.
:::

**Why `String` is immutable.** Because a string can never change, it can be shared
safely: by many variables, by many threads, and through the string pool. Its hash code can
be computed once and cached, which makes strings fast map keys (Chapter 7). And code that
receives a string, such as a file name or a password check, knows nobody can change it
afterwards.

**How strings are stored.** Since Java 9, OpenJDK stores a string whose characters all fit
in one byte (Latin-1) with one byte per character, and other strings with two bytes per
character (a feature called *compact strings*). Either way, `charAt` and `length` are O(1).
Methods that produce a new string, such as `substring`, `concat`, `replace` and
`toUpperCase`, copy characters and cost time proportional to the length of the result.

### Building strings efficiently

```{.java include="code-examples/ch01/StringBuilding.java" #lst-stringbuilding}
```

```{.output from="code-examples/ch01/StringBuilding"}
```

**Line by line.** Lines 34–37 show immutability: `toUpperCase` returns a new string, and
`word.concat("s")` creates `"stacks"` and throws it away, so `word` is still `"stack"`.
The naive method (lines 4–11) runs `result += i + ","` n times. Each `+=` creates a brand
new string and copies every character of the old one into it. The method
`charactersCopiedNaive` (lines 22–31) does not build anything; it adds up the length of
every intermediate string, which is the number of characters copied. The output shows the
effect: for n = 100,000 the final text has 588,890 characters, but `+=` copies about
29 billion characters to build it. The total is roughly proportional to n², which is why
the naive version takes seconds or minutes where `StringBuilder` takes milliseconds.

`StringBuilder` (lines 13–20) keeps one growable character array, like the `IntStack` of
Chapter 10, and `append` adds to its end in O(1) amortized time. `toString()` copies the
result into a new `String` once, at the end.

::: {.note title="What javac does with +"}
Since Java 9, the compiler turns a single expression like `a + b + c` into one efficient
call that sizes the result exactly, so one-line concatenations are fine. What it cannot do
is merge the iterations of a *loop*: each iteration is a separate concatenation that copies
the string built so far. Use `StringBuilder` whenever a string grows inside a loop.
:::

**Other `StringBuilder` methods (lines 46–51).** `insert`, `setCharAt`, `deleteCharAt`,
`charAt`, `length` and `reverse` work much like their names suggest. `append` returns the
builder itself, so calls can be chained. `insert` and `deleteCharAt` must shift the
characters after the position, so they cost O(n), except at the end.

**`StringBuffer`** has the same methods as `StringBuilder`, but each method is
`synchronized` so that several threads can share one buffer. That locking costs time and
is useless in single-threaded code. It is an older class (Java 1.0); `StringBuilder` was
added in Java 5 as the faster replacement. Use `StringBuilder`.

| Operation | Cost | Notes |
|-----------------------------------------|------------------|----------------------------------------|
| `s.length()`, `s.charAt(i)` | O(1) | |
| `s.substring(a, b)` | O(b − a) | copies the characters |
| `s + t`, `s.concat(t)` | O(n + m) | creates a new string |
| `s.equals(t)`, `s.compareTo(t)` | O(min(n, m)) | stops at the first difference |
| `s.hashCode()` | O(n) once, then O(1) | the hash is cached in the object |
| `s.indexOf(t)`, `s.contains(t)` | O(n × m) worst case | the API promises no algorithm; assume a simple scan |
| `s.toCharArray()`, `String.valueOf(chars)` | O(n) | both copy |
| `sb.append(x)` | O(1) amortized per character | |
| `sb.insert(i, x)`, `sb.deleteCharAt(i)` | O(n) | shifts the rest |
| `sb.reverse()`, `sb.toString()` | O(n) | |

: Costs of common String and StringBuilder operations (n and m are lengths)

### String methods you will use constantly

```{.java include="code-examples/ch01/StringMethods.java" #lst-stringmethods}
```

```{.output from="code-examples/ch01/StringMethods"}
```

Most of these are familiar; the important details are:

- `substring(2, 5)` (line 9) takes indices 2, 3 and 4: the end index is *excluded*. This
  half-open convention, from `start` included to `end` excluded, is used throughout the
  Java library, for example in `Arrays.copyOfRange` and `List.subList`.
- `toCharArray()` (line 11) returns a *copy* that you may sort or modify (line 12); the
  string itself cannot change. Sorting the characters of a string is a common first step
  in anagram problems (Chapter 11).
- `compareTo` (line 15) returns a negative number, zero or a positive number. For two
  strings that first differ at some position, it returns the difference of those two
  characters, so do not rely on the result being exactly −1 or 1.
- `split` takes a *regular expression*, not plain text (lines 18–21). A dot means "any
  character" in a regular expression, so `"1.5.2".split(".")` treats every character as a
  separator and returns an empty array; escape it as `"\\."`. `split` also drops empty
  strings at the end of the result, and `"\\s+"` splits on runs of whitespace. Call
  `strip()` first if the text may begin with whitespace, or the first element is `""`.
- `strip()` (Java 11) removes leading and trailing whitespace, including Unicode spaces;
  the older `trim()` removes only characters up to the space character.
- `Character.isLetterOrDigit`, `isDigit`, `isLetter` and `toLowerCase` (lines 26–28) work
  for all of Unicode. When input is guaranteed to be ASCII, comparisons like
  `ch >= 'a' && ch <= 'z'` are just as correct and slightly faster.

::: {.tip title="\"Why are strings immutable in Java?\""}
Give three reasons: *safety* (no one can change a string after another part of the program
has checked it, and immutable objects are automatically thread-safe), *sharing* (the
string pool can give every identical literal one shared object), and *speed in hash
tables* (the hash code is computed once and cached). Then mention the cost: building a
string piece by piece needs `StringBuilder`.
:::

## final, static, this and super {#keywords}

These four keywords appear in almost every program. The example shows what each one means
in practice.

```{.java include="code-examples/ch01/FinalAndStatic.java" #lst-final-static}
```

```{.output from="code-examples/ch01/FinalAndStatic"}
```

- **`static`** (lines 6–7) means "belongs to the class, not to one object". There is
  exactly one `created` counter however many objects exist, which is why line 46 prints
  2. In problem solving, `static` is used for helper methods and constants. Be careful
  with `static` *mutable* fields: if a judge runs several test cases inside one JVM, a
  static field keeps its value from one test case to the next. Reset it at the start of
  each test, or keep the state in local variables.
- **`final`** on a variable means it can be assigned only once. `static final` makes a
  constant such as `MOD` (line 6). A `final` field must be assigned exactly once, by the
  end of the constructor (lines 9–10, 13–14). `final` makes the *reference* fixed, not
  the object: line 19 may change the contents of `counts`, but line 20, if it were not a
  comment, would not compile. On a class, `final` forbids subclasses; on a method, it
  forbids overriding.
- **`this`** (line 13) refers to the current object. It is needed when a parameter has the
  same name as a field: `this.name` is the field, `name` the parameter. `this(...)` as the
  first statement of a constructor calls another constructor of the same class.
- **`super`** refers to the parent class. `super(name, 3)` on line 30 runs the parent's
  constructor, and `super.toString()` on line 35 calls the parent's version of a method
  that this class overrides. Chapter 3 covers inheritance and overriding in depth.

A local variable that is never reassigned is called *effectively final*, even without the
keyword. Lambdas (Chapter 2) may use local variables only if they are effectively final.

## null and NullPointerException {#null}

`null` is the value of a reference that points to no object. Using it as if it pointed to
an object, by calling a method, reading a field or an array element, or unboxing it,
throws a [NullPointerException]{.idx} (NPE).

```{.java include="code-examples/ch01/NullHandling.java" #lst-null}
```

```{.output from="code-examples/ch01/NullHandling"}
```

Since Java 15, NPE messages say exactly what was `null` and what the program tried to do
with it ("helpful NullPointerExceptions"; the feature arrived in Java 14, switched off by
default). The names `<local1>` and `<local2>` appear
because this book's examples are compiled without debugging information; compile with
`javac -g`, as IDEs do, and the messages show the real variable names.

The second NPE (lines 25–31) deserves attention, because it is the most common NPE in DSA
code. Unboxing a `null` `Integer` into an `int` throws. Exactly this happens with
`int count = map.get(key);` when the key is not in the map, because `get` returns `null`.
Use `map.getOrDefault(key, 0)` instead (Chapter 7).

**Habits that prevent most NPEs:**

- Write constant-first comparisons, `"yes".equals(answer)` (line 34), which are `false`
  rather than an exception when `answer` is `null`, or use `Objects.equals(a, b)` when
  either side may be `null` (lines 35–36).
- Check arguments at the start of public methods with
  `Objects.requireNonNull(value, "message")` (line 8). The program fails immediately,
  with a clear message, instead of somewhere far away.
- Use `Objects.requireNonNullElse(value, fallback)` (line 38) to substitute a default.
- Return empty arrays, strings and collections instead of `null` from your own methods.

## Arrays {#arrays}

Arrays are the most important data structure in DSA: fixed-size, indexed from 0, with
O(1) access to any element. Chapter 8 covers array algorithms; this section covers how
arrays behave in Java.

```{.java include="code-examples/ch01/ArraysBasics.java" #lst-arrays}
```

```{.output from="code-examples/ch01/ArraysBasics"}
```

**Creating arrays (lines 7–15).** `new int[4]` creates an array whose elements have the
default value of their type: `0`, `false` or `null`. An *array initializer* such as
`{2, 3, 5, 7}` creates and fills an array in one step. Every array has a `length` field
(no parentheses), fixed when the array is created; strings use `length()`, and
collections use `size()`.

**Two-dimensional arrays are arrays of arrays.** `new int[2][3]` creates an outer array of
2 references, each pointing to its own `int[3]`. Because every row is a separate array,
rows can have different lengths: line 12 creates only the outer array, and lines 13–15
give row `r` exactly `r + 1` slots. Such an array is called *jagged*.

```{.diagram caption="int[][] grid = new int[2][3] in memory"}
   grid ---> +-----+          +---+---+---+
             | ref +--------->| 0 | 0 | 0 |   grid[0]
             +-----+          +---+---+---+
             | ref +--------->| 0 | 0 | 0 |   grid[1]
             +-----+          +---+---+---+
```

**Copying (lines 21–25).** All four ways copy element by element in O(n):

- `clone()` returns an array of the same length with the same elements;
- `Arrays.copyOf(a, n)` copies into a new array of length `n`, padding with default values
  or cutting off the end;
- `Arrays.copyOfRange(a, from, to)` copies the half-open range `from` to `to - 1`;
- `System.arraycopy(src, srcPos, dest, destPos, length)` copies into an *existing* array;
  it also handles overlapping ranges within one array correctly.

**Shallow and deep copies (lines 29–36).** `clone()` on a 2D array copies the outer array
only: the copy contains the same row references, so changing `shallow[0][0]` also changes
`original[0][0]`, as the output shows. A [deep copy]{.idx} copies every row as well
(lines 32–35).

**The fill trap (lines 40–43).** `Arrays.fill(rows, oneRow)` stores *the same reference*
in every slot, so all three rows are one array, and writing `rows[0][0]` changes "every"
row. To create independent rows, allocate each row separately, or use `new int[3][3]`.

::: {.warning title="Printing arrays"}
`System.out.println(array)` prints something like `[I@1b6d3586`: the type and an identity
hash, not the contents. Use `Arrays.toString(array)` for one dimension and
`Arrays.deepToString(grid)` for more. In fast-output code (Chapter 2), print elements
with a `StringBuilder` instead.
:::

## Summary {#summary}

Java source code is compiled to bytecode, which the JVM verifies, interprets and, for hot
code, compiles to machine code. Java 21 is this book's baseline; online judges vary, so
choose the Java version explicitly. Primitive variables hold values; reference variables
hold references, and Java passes both *by value*. Integer arithmetic silently overflows,
integer division truncates toward zero, `%` keeps the sign of the dividend, and `double`
values are approximations; most of these traps are avoided by using `long`, casting before
multiplying, and comparing doubles with a tolerance. `==` compares references for objects,
so compare contents with `equals`, `Objects.equals` or `Arrays.equals`. Strings are
immutable, so build them with `StringBuilder`. Arrays have default values, 2D arrays are
arrays of arrays, and copies are shallow unless you copy every row.

| Topic | Remember |
|-------------------------|----------------------------------------------------------|
| Running Java | `javac` → bytecode → JVM (interpreter + JIT) |
| Versions | LTS: 8, 11, 17, 21, 25; this book targets 21; check your judge |
| Pass by value | methods get copies of values and of references |
| Overflow | silent; use `long`, cast before multiplying, `Math.addExact` to detect |
| Division | truncates toward zero; `%` has the sign of the dividend; `floorMod` |
| Doubles | compare with a tolerance; avoid for money |
| Midpoint | `low + (high - low) / 2` |
| Equality | `==` for primitives and identity, `equals` for content |
| `Integer` cache | only −128 to 127 is guaranteed; never compare boxes with `==` |
| Strings | immutable; `StringBuilder` in loops; `split` takes a regex |
| `null` | constant-first `equals`, `requireNonNull`, beware unboxing `map.get` |
| Arrays | `clone` of a 2D array is shallow; `Arrays.fill` with a row shares it |

: Key takeaways

## Exercises {#exercises}

::: {.exercise number="1.1" level="Easy" title="Predict the output"}
Without running it, write down what each line prints. Then explain each answer using the
rules from this chapter.

```{.fragment}
System.out.println(5 / 2 * 2.0);
System.out.println(5 / 2.0 * 2);
System.out.println('a' + 'b');
System.out.println("" + 'a' + 'b');
System.out.println(1 + 2 + "3" + 4 + 5);
System.out.println(-17 % 5);
System.out.println((byte) 200);
System.out.println(Integer.MIN_VALUE == -Integer.MIN_VALUE);
```
:::

::: {.exercise number="1.2" level="Easy" title="Fix the midpoint"}
A binary search computes `int mid = (low + high) / 2;` where `0 <= low <= high`. Give
values of `low` and `high` for which `mid` is wrong, and write two different correct
formulas. Explain why each one is correct.
:::

::: {.exercise number="1.3" level="Easy" title="The role check that only works in tests"}
A program checks `if (role == "admin")`, where `role` was read from the user. The
programmer's tests, which call the method with the literal `"admin"`, all pass, but real
users are never recognized as admins. Explain why, and fix the method so that it also
works when `role` is `null`.
:::

::: {.exercise number="1.4" level="Easy" title="A real deep copy"}
Write `int[][] deepCopy(int[][] source)` that returns a completely independent copy of a
2D array. It must work for jagged arrays, for rows that are `null`, and for `source`
itself being `null`.
:::

::: {.exercise number="1.5" level="Medium" title="Sums without silent overflow"}
Write two methods for an `int[]`: `long sumAsLong(int[] values)`, which always returns the
correct total, and `int sumExact(int[] values)`, which returns the total as an `int` but
throws `ArithmeticException` if any intermediate sum overflows. Can `sumAsLong` itself
overflow? Justify your answer.
:::

::: {.exercise number="1.6" level="Easy" title="Letter counts with an array"}
Write `String letterCounts(String text)` that counts the lowercase letters `a` to `z` in
`text` (ignoring every other character) and returns them in alphabetical order as letter
followed by count, skipping letters that do not occur. For example, `"banana"` gives
`"a3b1n2"`. Use an `int[26]`, not a map.
:::

::: {.exercise number="1.7" level="Medium" title="Reverse the words"}
Write `String reverseWords(String sentence)` that returns the words in reverse order,
separated by single spaces, with no leading or trailing spaces. Words are separated by one
or more whitespace characters. For example, `"  the sky   is blue "` gives
`"blue is sky the"`, and a string of only spaces gives `""`.
:::

::: {.exercise number="1.8" level="Easy" title="Trace the calls"}
Predict the output, and explain it in terms of values and references.

```{.fragment}
static void swapValues(int x, int y) { int t = x; x = y; y = t; }
static void swapFirstTwo(int[] a) { int t = a[0]; a[0] = a[1]; a[1] = t; }
static void replace(int[] a) { a = new int[] {7, 7}; a[0] = 8; }

int a = 1, b = 2;
int[] pair = {1, 2};
swapValues(a, b);
swapFirstTwo(pair);
replace(pair);
System.out.println(a + " " + b + " " + Arrays.toString(pair));
```
:::

::: {.exercise number="1.9" level="Medium" title="Valid palindrome"}
Write `boolean isPalindrome(String text)` that ignores every character that is not a
letter or digit, and ignores case. `"A man, a plan, a canal: Panama"` is a palindrome;
`"race a car"` is not. Use O(1) extra space: do not build a cleaned copy of the string.
:::

::: {.exercise number="1.10" level="Medium" title="Factorial modulo a prime"}
Compute n! modulo 1,000,000,007 for n up to 10^6^. Explain why
`int result = 1; result = result * i % MOD;` gives wrong answers even though every result
is reduced modulo `MOD`, and write a correct version.
:::

::: {.exercise number="1.11" level="Hard" title="Run-length encoding in linear time"}
Write `String encode(String text)` that replaces every run of equal characters with the
character followed by the run length: `"aaabcc"` becomes `"a3b1c2"`. Your method must run
in O(n). Then analyze a version that builds the result with `+=`: how many characters does
it copy in the worst case?
:::

::: {.exercise number="1.12" level="Hard" title="Detect overflow yourself"}
Without using `Math.multiplyExact`, write `boolean multiplyOverflows(int a, int b)` and
`boolean multiplyOverflows(long a, long b)`. For the `long` version there is no larger
primitive type to compute in. Test your methods on the edge cases `0`, `1`, `-1`,
`Long.MIN_VALUE` and `Long.MAX_VALUE`.
:::

## Solutions {#solutions}

### Diagnostic quiz answers {#quiz-answers}

1. Bytecode in `.class` files; the JVM runs it. (See []{.ref to="how-java-runs"}.)
2. `4`: `7 / 2` is 3 and `7 % 2` is 1. ([]{.ref to="division"})
3. No; the largest `int` is 2,147,483,647. Use `long`. ([]{.ref to="primitive-vs-reference"})
4. −1,794,967,296: the `int` product overflows before it is stored in the `long`.
   ([]{.ref to="overflow"})
5. 9: `a` and `b` refer to the same array. ([]{.ref to="primitive-vs-reference"})
6. Always pass by value; for objects the value is a reference.
   ([]{.ref to="primitive-vs-reference"})
7. `false`: `==` compares references and `new String` creates a new object.
   ([]{.ref to="equality"})
8. Each `+=` copies the whole string so far, so the total work grows with the square of
   the length. ([]{.ref to="strings"})
9. `false` with default settings: only −128 to 127 are guaranteed to be cached.
   ([]{.ref to="equality"})
10. `false, false, false` and `null, null, null`. ([]{.ref to="arrays"})
11. `NullPointerException`: `get` returns `null`, and unboxing `null` throws.
    ([]{.ref to="null"})
12. Same methods; `StringBuffer` synchronizes every call and is slower. Use
    `StringBuilder`. ([]{.ref to="strings"})

If you missed three or more questions, read the whole chapter carefully. Otherwise, read
the sections for the questions you missed and skim the rest.

::: {.solution number="1.1"}
```{.output from="solutions/ch01/PredictOutput"}
```

- `5 / 2 * 2.0`: `5 / 2` is integer division, 2; then `2 * 2.0` is 4.0.
- `5 / 2.0 * 2`: `5 / 2.0` is 2.5; times 2 is 5.0.
- `'a' + 'b'`: both `char`s are promoted to `int`: 97 + 98 = 195.
- `"" + 'a' + 'b'`: `+` is evaluated left to right; `"" + 'a'` is the string `"a"`, so
  the next `+` also concatenates: `"ab"`.
- `1 + 2 + "3" + 4 + 5`: `1 + 2` is 3 (numbers); then `3 + "3"` is `"33"`, and every
  later `+` concatenates: `"3345"`.
- `-17 % 5`: the remainder has the sign of the dividend: −2.
- `(byte) 200`: 200 does not fit in a `byte` (−128 to 127). The cast keeps the lowest 8
  bits, which represent 200 − 256 = −56.
- `Integer.MIN_VALUE == -Integer.MIN_VALUE`: negating −2^31^ gives 2^31^, which overflows
  back to −2^31^. So the value is its own negation, and the comparison is `true`. For the
  same reason, `Math.abs(Integer.MIN_VALUE)` is negative.
:::

::: {.solution number="1.2"}
With `low = 1_500_000_000` and `high = 2_000_000_000`, the sum 3.5 × 10^9^ overflows and
the "middle" becomes negative.

```{.java include="solutions/ch01/Midpoint.java" region="solve" kind="Methods"}
```

```{.output from="solutions/ch01/Midpoint"}
```

`low + (high - low) / 2` is correct because `high - low` is between 0 and `high`, so it
fits, and adding half of it to `low` gives a value between `low` and `high`, which also
fits. `(low + high) >>> 1` is correct for non-negative `low` and `high` for a different
reason: the true sum is below 2^32^, so even when it overflows into a negative `int`, its
32 bits are still the correct *unsigned* value. The unsigned right shift `>>>` divides that
unsigned value by 2 (Chapter 19). The first formula is the clearest; use it.
:::

::: {.solution number="1.3"}
`==` compares references. A literal `"admin"` in the test and the literal inside the
method are the same pooled object, so the buggy version passes the test. Text typed by a
user is built at run time into a *new* `String` object, so `==` is `false` even though the
characters are equal.

```{.java include="solutions/ch01/RoleCheck.java" region="solve" kind="Methods"}
```

```{.output from="solutions/ch01/RoleCheck"}
```

Writing the constant first, `"admin".equals(role)`, compares characters, and it returns
`false` instead of throwing when `role` is `null`. The lesson for testing is that tests
built only from literals can hide this bug; include input built at run time.
:::

::: {.solution number="1.4"}
Copy the outer array, then copy every row that is not `null`.

```{.java include="solutions/ch01/DeepCopy.java" region="solve"}
```

```{.output from="solutions/ch01/DeepCopy"}
```

Changing the copy, both an element and a whole row, leaves the original unchanged. The
method copies every element once, so it runs in time proportional to the total number of
elements. For deeper nesting (`int[][][]`), the same idea is applied recursively.
:::

::: {.solution number="1.5"}
```{.java include="solutions/ch01/SafeSum.java" region="solve" kind="Methods"}
```

```{.output from="solutions/ch01/SafeSum"}
```

`sumAsLong` cannot overflow for any array Java can create. An array has at most about
2^31^ elements, and each `int` has absolute value at most 2^31^, so the total is at most
about 2^62^ in absolute value, below the `long` limit of about 2^63^. In `sumExact`, the
check must happen at *every* addition, as `Math.addExact` does: an intermediate sum can
overflow even when the final total would fit, as in the example, where
2 × 10^9^ + 2 × 10^9^ overflows before the −10^9^ is added.
:::

::: {.solution number="1.6"}
```{.java include="solutions/ch01/LetterCounts.java" region="solve"}
```

```{.output from="solutions/ch01/LetterCounts"}
```

`ch - 'a'` maps `'a'` to 0 through `'z'` to 25, and `(char) ('a' + letter)` maps back.
The method is O(n + 26) = O(n) time with O(1) extra space, since the array always has 26
elements. An array is preferred over a `HashMap<Character, Integer>` here because it
avoids boxing every character and count, and it keeps the letters in order for free.
:::

::: {.solution number="1.7"}
```{.java include="solutions/ch01/ReverseWords.java" region="solve"}
```

```{.output from="solutions/ch01/ReverseWords"}
```

`strip()` removes the outer spaces first; otherwise `split` would return an empty first
element for the leading spaces. The empty-string check is needed because `"".split(...)`
returns an array containing one empty string. The method is O(n). An in-place version over
a `char[]` (reverse the whole array, then reverse each word) is a classic follow-up; it
uses the two-pointer technique of Chapter 8.
:::

::: {.solution number="1.8"}
```{.output from="solutions/ch01/SwapTrace"}
```

`swapValues` swaps its own copies of the two values, so `a` and `b` are unchanged.
`swapFirstTwo` receives a copy of the *reference* to `pair` and swaps the elements of that
same array, so `pair` becomes `[2, 1]`. `replace` makes its copy of the reference point to
a new array and changes that new array; the caller's `pair` is untouched.
:::

::: {.solution number="1.9"}
Use two pointers from the two ends. Skip characters that are not letters or digits, and
compare the rest case-insensitively.

```{.java include="solutions/ch01/Palindrome.java" region="solve"}
```

```{.output from="solutions/ch01/Palindrome"}
```

Each iteration moves at least one pointer inward, so the loop runs at most n times: O(n)
time and O(1) extra space. A string with no letters or digits counts as a palindrome. The
two-pointer pattern itself is the subject of Chapter 8.
:::

::: {.solution number="1.10"}
```{.java include="solutions/ch01/FactorialMod.java" region="solve"}
```

```{.output from="solutions/ch01/FactorialMod"}
```

`result * i % MOD` is evaluated as `(result * i) % MOD`. With `int` variables, the
product of a value just below 10^9^ and `i` overflows *before* `%` is applied, so the
remainder is taken of an already wrong number. The correct version stores `result` as a
`long`: the product is below about 10^9^ × 10^6^ = 10^15^, which fits easily in a `long`,
and the remainder is below `MOD`, so it fits back into an `int` at the end. The buggy
output above (from `factorialModBuggy` in the same file) is already wrong at n = 13,
because 13! is larger than the largest `int`.
:::

::: {.solution number="1.11"}
```{.java include="solutions/ch01/RunLength.java" region="solve"}
```

```{.output from="solutions/ch01/RunLength"}
```

Each character is examined once by the inner loop and the outer index jumps to the end of
each run, so the method is O(n), and `StringBuilder` makes each append O(1) amortized.

With `+=`, the worst case is a string with no repeated neighbours, such as `"abab…"`,
which produces n runs. Each `+=` copies the whole result so far, whose length after k runs
is at least 2k (a character and at least one digit). The total number of characters copied
is at least 2 + 4 + … + 2n = n(n + 1), so the `+=` version is O(n²).
:::

::: {.solution number="1.12"}
```{.java include="solutions/ch01/MultiplyOverflow.java" region="solve" kind="Methods"}
```

```{.output from="solutions/ch01/MultiplyOverflow"}
```

For `int`, the exact product always fits in a `long` (it is at most 2^62^ in absolute
value), so compute it there and compare with the `int` range. For `long`, multiply anyway
and divide back: if no overflow happened, `product / b` equals `a`. If the product wrapped
around, the division cannot give `a` back. One case escapes this test: `Long.MIN_VALUE * -1`
wraps to `Long.MIN_VALUE`, and `Long.MIN_VALUE / -1` *also* overflows to
`Long.MIN_VALUE`, so that pair is checked separately. The output shows the boundaries:
46,340² fits in an `int` but 46,341² does not, and 3,037,000,499² fits in a `long` but
3,037,000,500² does not. The self-check program compares both methods with
`Math.multiplyExact` on thousands of pairs, including all the edge cases.
:::
