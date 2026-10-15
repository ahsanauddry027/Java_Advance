# Glossary

Short definitions of the technical terms used in this book, in alphabetical order. The
chapter in parentheses is where the term is explained in full.

Algorithm
:   A precise, finite sequence of steps that solves a problem. (How to Use This Book)

Amortized cost
:   The average cost per operation over a worst-case *sequence* of operations. An operation
    can occasionally be expensive (for example, resizing an array) and still be O(1)
    amortized if the expensive cases are rare enough. (Chapters 6 and 10)

ArrayDeque
:   Java's resizable, circular-array implementation of the `Deque` interface; the
    recommended class for stacks and queues. It does not allow `null` elements. (Chapter 10)

Autoboxing
:   The automatic conversion of a primitive value, such as an `int` or `char`, into its
    wrapper object, such as `Integer` or `Character`. The reverse conversion is called
    *unboxing*. (Chapter 5)

Breadth-first search (BFS)
:   A way to explore a graph or grid level by level, using a queue, so that nodes are
    visited in order of their distance from the start. In unweighted graphs, it finds
    shortest paths. (Chapters 10 and 17)

Bytecode
:   The instruction set of the Java Virtual Machine, stored in `.class` files by the
    compiler. The same bytecode runs on every operating system. (Chapter 1)

Circular buffer
:   An array used as a ring: an index that moves past the last slot wraps around to slot 0.
    Also called a *ring buffer*. It lets a queue add and remove in O(1) without shifting
    elements. (Chapter 10)

Deep copy
:   A copy that duplicates an object *and* everything it refers to, so that changing the
    copy never affects the original. For a 2D array, every row must be copied. Compare
    *shallow copy*. (Chapter 1)

Deque
:   A *double-ended queue*: a collection that allows insertion, removal and inspection at
    both ends. Pronounced "deck". (Chapter 10)

FIFO (first in, first out)
:   The rule of a queue: the element that has waited longest is removed first. (Chapter 10)

Immutable
:   Describes an object whose state can never change after it is created, such as a `String`
    or an `Integer`. (Chapters 1 and 3)

Infix notation
:   The usual way of writing expressions, with each operator between its operands, as in `3
    + 4`. (Chapter 10)

Integer overflow
:   What happens when the exact result of an `int` or `long` operation does not fit in the
    type: Java keeps only the lowest bits, so the result silently wraps around. (Chapters 1
    and 19)

Invariant
:   A condition that an algorithm keeps true at a fixed point, for example at the start of
    every loop iteration. Stating the invariant is often the clearest way to explain why an
    algorithm is correct. (How to Use This Book)

JDK (Java Development Kit)
:   The runtime (JVM and class library) plus development tools such as `javac`, `javap` and
    `jshell`. (Chapter 1)

JIT compiler
:   The *just-in-time* compiler inside the JVM that translates frequently executed bytecode
    into native machine code while the program runs. (Chapters 1 and 5)

JVM (Java Virtual Machine)
:   The program that loads `.class` files, verifies their bytecode and executes it.
    (Chapters 1 and 5)

LIFO (last in, first out)
:   The rule of a stack: the element added most recently is removed first. (Chapter 10)

LTS (long-term support)
:   A Java release that vendors support for several years. Java 8, 11, 17, 21 and 25 are LTS
    releases. (Chapter 1)

Monotonic queue
:   A deque whose elements are kept in sorted order from front to back. New elements remove
    the elements they dominate from the back; expired elements leave from the front. Used
    for sliding-window maximum and minimum. (Chapter 10)

Monotonic stack
:   A stack whose elements are kept in sorted order from bottom to top. Before a push, the
    elements that would break the order are popped. Used for "next greater" and "previous
    smaller" problems in O(n). (Chapter 10)

NullPointerException
:   The exception thrown when a program uses a `null` reference as if it pointed to an
    object, including when it unboxes `null`. (Chapter 1)

Pass by value
:   Java's only parameter-passing rule: a method receives copies of its arguments. For
    objects and arrays, the copied value is a reference. (Chapter 1)

Postfix notation
:   A way of writing expressions with each operator after its operands, as in `3 4 +`. Also
    called *Reverse Polish notation* (RPN). It needs no parentheses or precedence rules.
    (Chapter 10)

Primitive type
:   One of Java's eight built-in value types: `byte`, `short`, `int`, `long`, `float`,
    `double`, `char` and `boolean`. A primitive variable holds the value itself. (Chapter 1)

Queue
:   A collection where elements are added at the back and removed from the front, in
    first-in, first-out order. (Chapter 10)

Reference type
:   Any class, interface, array or enum type. A variable of a reference type holds a
    reference to an object, or `null`. (Chapter 1)

Shallow copy
:   A copy of an object that shares the objects it refers to with the original. `clone()` on
    a 2D array is shallow: the rows are shared. (Chapter 1)

Stack
:   A collection where elements are added and removed at the same end, the top, in last-in,
    first-out order. (Chapter 10)

String pool
:   The JVM's table of shared `String` objects. Every string literal, and every compile-time
    constant string, refers to the single pooled object with that text. (Chapter 1)

Widening and narrowing conversions
:   Converting a number to a type with a larger range (widening, automatic) or a smaller one
    (narrowing, which needs a cast and may lose information). (Chapter 1)
