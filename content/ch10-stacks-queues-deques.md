# Stacks, Queues and Deques {#stacks-queues-deques}

::: objectives
**After this chapter you will be able to:**

- explain what stacks, queues and deques are, and why each one exists;
- implement a stack with an array and with linked nodes, and a queue with a circular array;
- use `Deque` and `ArrayDeque` correctly as a stack and as a queue, and explain why the
  legacy `Stack` class is usually avoided;
- solve bracket-matching and expression-evaluation problems with stacks;
- recognize and apply the *monotonic stack* and *monotonic queue* patterns;
- explain why a queue gives shortest paths in breadth-first search;
- analyze all of these, including their amortized costs, and discuss them in an interview.

**Prerequisites:** Big-O notation and amortized analysis (Chapter 6); the `Deque`
interface's place in the Collections Framework (Chapter 7); linked nodes (Chapter 9).
:::

::: quiz
1. You push 1, 2 and 3 onto a stack and then pop once. Which value comes out?
2. You add 1, 2 and 3 to a queue and then remove once. Which value comes out?
3. What does `peek()` return on an empty `ArrayDeque`, and what does `pop()` do?
4. A growable array doubles its size when it is full. Why is pushing onto it "O(1)
   amortized" rather than simply O(1)?
5. Name one problem you have solved (in any language) that needed "the most recent
   unfinished thing" first.
:::

A surprising number of problems have the same shape: things arrive one at a time, and
the program must decide *which waiting thing to deal with next*. If the answer is "the
most recent one", you need a stack. If it is "the oldest one", you need a queue. If you
sometimes need one end and sometimes the other, you need a deque. These three structures
are simple, but they are the engine inside many famous algorithms: bracket matching,
expression evaluation, depth-first and breadth-first search, and the monotonic-stack
techniques that turn many O(n²) solutions into O(n) ones.

## Stacks: last in, first out {#stacks}

::: {.definition title="Stack"}
A [stack]{.idx} is a collection where the element removed next is always the one added
most recently. This rule is called [LIFO]{.idx term="LIFO (last in, first out)"}:
*last in, first out*. The end where elements are added and removed is the **top**.
:::

**Why it exists.** Many tasks must be finished in the reverse order of how they were
started. When you open a bracket `(` and then a bracket `[`, the `[` must be closed
first. When method `a` calls `b` and `b` calls `c`, then `c` returns first, then `b`, then
`a`; the JVM keeps these unfinished calls on a stack, which is why it is called the *call
stack* (Chapter 5). An editor's *undo* list, a browser's *back* button, and depth-first
search all follow the same rule.

**How it works.** A stack offers a few operations, and all of them touch only the top:

| Operation | Meaning | Typical cost |
|-----------|--------------------------------------------------|--------------|
| `push(x)` | put `x` on top | O(1) |
| `pop()` | remove the top element and return it | O(1) |
| `peek()` | return the top element without removing it | O(1) |
| `isEmpty()` | is the stack empty? | O(1) |
| `size()` | how many elements are on the stack? | O(1) |

: The stack operations

```{.diagram caption="A stack after push(10), push(20), push(30), then pop()"}
  push(10)          push(20)          push(30)          pop() -> 30

                                      | 30 | <- top
                    | 20 | <- top     | 20 |            | 20 | <- top
  | 10 | <- top     | 10 |            | 10 |            | 10 |
  +----+            +----+            +----+            +----+
```

Because only the top is ever touched, a stack never needs to search or shift anything.
That is why every operation can be O(1). The next two sections build a stack twice, once
on an array and once on linked nodes, because the two designs show different trade-offs
that interviewers like to ask about.

### Implementing a stack with an array {#array-stack}

The simplest stack keeps its elements in an array. The bottom element sits at index 0
and the top element at index `size - 1`. Pushing writes to index `size`; popping reads
from index `size - 1`. When the array is full, we create a bigger one and copy the
elements across.

```{.java include="code-examples/ch10/IntStack.java" #lst-intstack}
```

**Syntax breakdown.**

- `private int[] items = new int[2];` (line 7) declares a field and gives it a starting
  value. Every new `IntStack` object starts with an array of two `int`s, all `0`. A real
  implementation would start larger; two is used here so that you can watch the array grow.
- `private int size;` (line 8) has no initializer. Fields of type `int` start at `0`, so a
  new stack is empty. (Local variables are different: Java refuses to compile code that
  reads a local variable before assigning it.)
- `Arrays.copyOf(items, items.length * 2)` (line 12) creates a *new* array of the given
  length, copies the old elements into the front of it, and fills the rest with zeros.
  The old array is no longer referenced and the garbage collector can reclaim it.
- `throw new NoSuchElementException(...)` (line 20) creates an exception object and
  throws it. `NoSuchElementException` is an *unchecked* exception from `java.util`, the
  same one that `ArrayDeque` throws, so callers are not forced to catch it.
- `int capacity()` (line 41) has no access modifier, so it is *package-private*: other
  classes in the same folder (the same package) can call it, but it is not part of the
  public interface. The demonstration and the self-check program use it.
- `System.out.printf("push %d  -> ...%n", ...)` (lines 49–50) formats text: each `%d` is
  replaced by the next `int` argument, and `%n` writes the platform's line separator.

**Line by line.** `push` first checks whether the array is full (line 11). If so, line 12
replaces `items` with a copy twice as long. Line 14 stores the value in the first free slot,
and line 15 records that the stack is one element bigger. `pop` refuses to work on an empty
stack (lines 19–21); otherwise it shrinks `size` (line 22) and returns the element that
was on top (line 23). Lines 22 and 23 could be written as `return items[--size];`, but two
separate lines are easier to read. `peek` is the same as `pop` except that it does not
change `size`.

The `main` method pushes five values, prints the size and capacity after each push, and
then pops everything:

```{.output from="code-examples/ch10/IntStack"}
```

The capacity goes 2, 2, 4, 4, 8: the array doubles exactly when a push finds it full.
The values come out in reverse order, which is the LIFO rule in action. The last line
shows the exception message when we pop from an empty stack.

::: {.keyidea title="Doubling makes push O(1) amortized"}
A push that triggers a resize copies every element, so it costs O(n). But resizes are
rare: after doubling from n to 2n slots, the next n pushes are cheap. Over any sequence of
n pushes, the copying work is at most 1 + 2 + 4 + … + n < 2n element copies, so the
*average* cost per push is a constant. We say push is
[O(1) amortized]{.idx term="amortized cost"}. If the array grew by a fixed amount
instead (say, 10 slots), the copying would add up to about n²/20, and push would become
O(n) on average. Chapter 6 explains amortized analysis in detail.
:::

| Operation | Time | Why |
|------------|--------------------|--------------------------------------------------|
| `push` | O(1) amortized, O(n) worst case | a resize copies all n elements, but rarely |
| `pop`, `peek` | O(1) | one array access |
| `isEmpty`, `size` | O(1) | read one field |
| Space | O(n) | the array is at most about twice as large as needed |

: Cost of the array-based stack

::: {.warning title="Obsolete references in object stacks"}
`IntStack` stores `int` values, so leaving an old value in the array after `pop` is
harmless. A stack of *objects* must also clear the slot (`items[size] = null;`) after
popping. Otherwise the array still refers to the popped object, the garbage collector
cannot reclaim it, and the program slowly leaks memory. Joshua Bloch's *Effective Java*
uses exactly this stack bug as its main example of an "obsolete reference".
:::

### Implementing a stack with linked nodes {#linked-stack}

A stack can also be a chain of nodes, where each node holds one value and a reference to
the node below it. The stack only needs to remember the top node.

```{.java include="code-examples/ch10/LinkedStack.java" #lst-linkedstack}
```

**Syntax breakdown.**

- `public class LinkedStack<T>` (line 4) declares a *generic* class. `T` is a type
  parameter: a `LinkedStack<String>` holds strings and a `LinkedStack<Integer>` holds
  integers, and the compiler checks every use. Chapter 3 covers generics in depth.
- `private static final class Node<E>` (line 6) is a *static nested class*. `static`
  means a `Node` object does not carry a hidden reference to a `LinkedStack` object,
  which saves memory and is the usual choice for node classes. `final` means no class can
  extend it. It declares its own type parameter `E`; when `LinkedStack<T>` writes
  `Node<T>`, `E` becomes `T`.
- `final E value;` and `final Node<E> next;` (lines 7–8) are assigned once, in the
  constructor, and never change. Nodes in this stack are never modified after creation.
- `new Node<>(value, top)` (line 20) uses the *diamond operator* `<>`: the compiler works
  out that the type is `Node<T>` from the variable it is assigned to.

**How push and pop work.** `push` creates a node whose `next` points to the current top,
then makes the new node the top (line 20). `pop` saves the top value (line 28) and moves
`top` down one node (line 29). Nothing refers to the old top node any more, so the
garbage collector can reclaim it; unlike the array version, no explicit clean-up is needed.

```{.diagram caption="LinkedStack after pushing \"home\", \"search\" and \"results\""}
  top
   |
   v
 +-----------+------+     +----------+------+     +--------+------+
 | "results" | next-+---->| "search" | next-+---->| "home" | null |
 +-----------+------+     +----------+------+     +--------+------+
```

```{.output from="code-examples/ch10/LinkedStack"}
```

**Array or linked nodes?** Both give O(1) push and pop, but they differ in detail:

| | Array-based | Linked nodes |
|-----------------|---------------------------------|-----------------------------------|
| push | O(1) amortized; occasionally O(n) when it resizes | O(1) every time |
| Memory per element | one array slot (4 bytes for `int`), plus unused capacity | a whole node object: header, value reference, next reference |
| Memory locality | elements sit next to each other, which is cache-friendly | nodes are scattered across the heap |
| Typical speed | faster in practice | slower in practice |

: Two ways to build a stack

The exact size of an object depends on the JVM (Chapter 5), but a node is always much
larger than one array slot. For coding interviews and online judges, an array-based
stack, either your own `int[]` or Java's `ArrayDeque`, is almost always the right choice.

## Queues: first in, first out {#queues}

::: {.definition title="Queue"}
A [queue]{.idx} is a collection where the element removed next is always the one that has
waited longest. This rule is called [FIFO]{.idx term="FIFO (first in, first out)"}:
*first in, first out*. Elements join at the **back** (or *tail*) and leave from the
**front** (or *head*).
:::

**Why it exists.** Whenever work must be handled fairly, in arrival order, a queue is the
natural model: print jobs, requests to a web server, messages between programs, and the
cells of a maze explored by breadth-first search ([]{.ref to="bfs-preview"}). The
operations are the queue versions of push and pop:

| Operation | Meaning | Typical cost |
|-------------|--------------------------------------------|--------------|
| `enqueue(x)` | add `x` at the back | O(1) |
| `dequeue()` | remove and return the front element | O(1) |
| `peek()` | return the front element without removing it | O(1) |

: The queue operations

The obvious array implementation is too slow. If the front element always lives at index
0, then every `dequeue` must shift all remaining elements one place left, which is O(n).
The fix is to let the front *move* instead of the elements.

### A circular array queue {#circular-queue}

Keep an index `head` that points at the front element, and a count `size`. The back of
the queue is `size` slots after `head`. When an index runs off the end of the array, it
wraps around to 0, as if the array were bent into a ring. This structure is called a
[circular buffer]{.idx} or *ring buffer*.

```{.diagram caption="A circular array of capacity 4 holding the queue 3, 4, 5, 6"}
   index:      0     1     2     3
            +-----+-----+-----+-----+
   items:   |  5  |  6  |  3  |  4  |        queue order: 3, 4, 5, 6
            +-----+-----+-----+-----+
                         ^
                        head = 2, size = 4

   front = items[head]                    = items[2] = 3
   back  = items[(head + size - 1) % 4]   = items[1] = 6
```

The listing shows only the queue logic. The complete file in `code-examples/ch10` also has
a `main` method and a small `layout` method that prints the raw array, with `_` for
unused slots.

```{.java include="code-examples/ch10/IntQueue.java" region="queue" kind="Fields and methods" #lst-intqueue}
```

**Syntax breakdown.** The `%` operator (lines 9, 19 and 42) is the *remainder* operator:
`a % b` is what is left after dividing `a` by `b`. With a capacity of 4, the index after
3 is `(3 + 1) % 4 = 0`, which is how the queue wraps around. Both operands are
non-negative here, which matters: in Java, `-1 % 4` is `-1`, not `3`.

**Line by line.** `enqueue` grows the array if it is full (lines 6–8), computes the first
free slot after the back (line 9), stores the value there (line 10) and increases `size`
(line 11). `dequeue` refuses to work on an empty queue (lines 15–17), reads the front value
(line 18), moves `head` forward with wrap-around (line 19), and decreases `size` (line 20).
`grow` (lines 39–46) cannot simply call `Arrays.copyOf`, because the queue may wrap around
the end of the old array. It copies the elements *in queue order* into the start of a new
array (lines 41–43) and resets `head` to 0 (line 45).

The demonstration enqueues 1 to 4, dequeues twice, enqueues 5 and 6 (which wrap around to
the start of the array), and then enqueues 7, which forces a resize:

```{.output from="code-examples/ch10/IntQueue"}
```

Notice the fourth line: the array is `[5 6 3 4]`, but the queue is 3, 4, 5, 6, because
`head` is 2. After the resize, the elements are stored in order again starting at index 0.

::: {.warning title="Full or empty?"}
Some textbook queues store only `head` and `tail` indices. Then `head == tail` could mean
"empty" or "full", and the code needs an extra rule, such as always leaving one slot
unused. Keeping an explicit `size`, as `IntQueue` does, removes the ambiguity. Exercise
10.3 asks you to build a fixed-capacity version with this design.
:::

| Operation | Time |
|------------------|------------------------------------------|
| `enqueue` | O(1) amortized (O(n) when it resizes) |
| `dequeue`, `peek` | O(1) |
| Space | O(n) |

: Cost of the circular array queue

## Deques and Java's ArrayDeque {#deques}

::: {.definition title="Deque"}
A [deque]{.idx term="deque (double-ended queue)"} (pronounced "deck", short for
*double-ended queue*) allows insertion, removal and inspection at **both** ends. A deque
can therefore act as a stack (use one end) or as a queue (add at one end, remove at the
other).
:::

In Java you will rarely write your own stack or queue. The `java.util.Deque` interface
describes a deque, and `java.util.ArrayDeque` implements it with a circular array, just
like `IntQueue` but generic and growable at both ends. Each operation comes in two
forms: one throws an exception when it fails, the other returns a special value (`null`
or `false`).

| Operation | Throws an exception on failure | Returns a special value |
|---------------------|------------------------------------|-----------------------------|
| Insert at front | `addFirst(e)`, `push(e)` | `offerFirst(e)` |
| Insert at back | `addLast(e)`, `add(e)` | `offerLast(e)`, `offer(e)` |
| Remove from front | `removeFirst()`, `pop()`, `remove()` | `pollFirst()`, `poll()` |
| Remove from back | `removeLast()` | `pollLast()` |
| Look at front | `getFirst()`, `element()` | `peekFirst()`, `peek()` |
| Look at back | `getLast()` | `peekLast()` |

: The Deque methods (Java SE 21 API)

Two consequences of this table matter in every problem:

- **As a stack**, use `push`, `pop` and `peek`. They all work on the *front* of the deque,
  so the front is the top of the stack.
- **As a queue**, use `offer`, `poll` and `peek`. `offer` adds at the *back*; `poll` and
  `peek` work on the front.

`peek` is the same method in both roles: it always looks at the front. On an empty deque,
`peek` and `poll` return `null`, while `pop`, `remove` and `getFirst` throw
`NoSuchElementException`. The program below shows all of this.

```{.java include="code-examples/ch10/DequeTour.java" #lst-dequetour}
```

```{.output from="code-examples/ch10/DequeTour"}
```

**What the output shows.**

- Lines 10–16: after pushing 1, 2, 3, printing the deque shows `[3, 2, 1]`. The
  `toString` method lists elements from the front, and `push` adds at the front, so the
  stack prints from the top down. `pop` returns 3, and then `peek` sees 2.
- Lines 18–24: after offering 1, 2, 3, the deque prints `[1, 2, 3]`: front first. `poll`
  removes 1, the oldest element.
- Lines 26–38: on an empty deque, `peek` and `poll` return `null`, `pop` throws, and
  `push(null)` throws `NullPointerException`. `ArrayDeque` forbids `null` elements because
  `null` is the value that `poll` and `peek` use to say "empty"; a stored `null` would be
  impossible to tell apart from an empty deque.
- Lines 40–46: the legacy `Stack` class prints from the *bottom* up, and it happily
  accepts `add(0, 99)`, which inserts at the bottom of a "stack". The next section
  explains why that matters.

**Inside ArrayDeque.** In OpenJDK 21, `ArrayDeque` stores its elements in an `Object[]`
used as a circular array, with a `head` index and a `tail` index. `addFirst` (and so
`push`) moves `head` one step *backwards* with wrap-around and stores the element there;
`addLast` (and so `offer`) stores at `tail` and moves it forward. A new `ArrayDeque()`
starts with 17 slots; when the array is full it doubles while small and then grows by 50%
at a time. Those numbers are implementation details that can change between versions.
What the API documentation *guarantees* is the behavior: most operations run in amortized
constant time, while `remove(Object)`, `contains`, `removeFirstOccurrence`,
`removeLastOccurrence` and iterator removal are linear.

::: {.note title="Java 21: sequenced collections"}
Since Java 21, `Deque` extends the new `SequencedCollection` interface, which adds
`reversed()`. For example, `stack.reversed()` gives a view of a stack from the bottom up,
which is handy for printing. Methods such as `getFirst()` and `getLast()` already existed
on `Deque`.
:::

### Why not java.util.Stack? {#legacy-stack}

Java has had a class called `Stack` since version 1.0, and many old tutorials use it.
Its own documentation now says that the `Deque` interface "should be used in preference
to this class". There are three concrete reasons:

1. **It is a `Vector`.** `Stack` extends `Vector`, an early growable array whose methods
   are all `synchronized`, which means they take a lock on every call so that several
   threads can share it. A single-threaded algorithm pays for that locking and gains
   nothing.
2. **It breaks the stack rule.** Because `Stack` inherits every `Vector` method, code can
   call `add(0, x)`, `remove(index)` or `get(index)` on it, as `DequeTour` did. A data
   structure that allows those operations is not really a stack.
3. **Its iteration order is surprising.** Iterating over a `Stack` (or printing it) goes
   from the bottom up, while an `ArrayDeque` used with `push` goes from the top down. Code
   that switches between them can silently change behavior.

`LinkedList` also implements `Deque`, so it can act as a stack or queue too. It allows
`null` elements and allocates a node object for every element, so it uses more memory,
and the `ArrayDeque` documentation notes that `ArrayDeque` is likely to be faster when
used as a queue.

| | `ArrayDeque` | `Stack` | `LinkedList` |
|-----------------|----------------------|----------------------|----------------------|
| Structure | circular array | growable array (`Vector`) | doubly linked nodes |
| Thread-safe | no | yes (synchronized) | no |
| `null` elements | not allowed | allowed | allowed |
| Index access (`get(i)`) | no | yes (breaks LIFO) | yes, O(n) |
| Iteration order as a stack | top to bottom | bottom to top | top to bottom |
| Recommended for stacks and queues | **yes** | no | rarely |

: ArrayDeque, Stack and LinkedList compared

::: {.warning title="Comparing boxed values with =="}
A `Deque<Integer>` stores `Integer` objects, not `int` values. The fragment below looks
right but compares two *references*:

```{.fragment}
if (stackA.peek() == stackB.peek()) { ... }       // wrong: compares objects
if (stackA.peek().equals(stackB.peek())) { ... }  // right: compares values
int a = stackA.peek();                            // also right: unbox first,
if (a == stackB.peek()) { ... }                   // then int == Integer unboxes
```

The wrong version often *passes small tests*, because Java caches `Integer` objects for
values from -128 to 127, and fails on larger values. Chapter 5 explains the cache. When at
least one side of `==` is a primitive `int`, Java unboxes the other side and compares
values.
:::

::: {.tip title="What to say when you declare a stack"}
Write `Deque<Integer> stack = new ArrayDeque<>();` and, if the interviewer seems
interested, add one sentence: "I'm using `ArrayDeque` instead of `Stack` because `Stack`
is a synchronized `Vector` and isn't recommended any more." It shows you know the library,
and it costs five seconds.
:::

## Balanced brackets {#balanced}

**The problem.** Given a string, decide whether its brackets `()`, `[]` and `{}` are
balanced: every opening bracket is closed by the *same kind* of bracket, in the right
order. Other characters are ignored. For example, `"([]{})"` and `"a[b(c)d]e"` are
balanced; `"([)]"`, `"(()"` and `"())"` are not. This is one of the most common first
questions in coding interviews (LeetCode 20, *Valid Parentheses*, is the standard
version).

**A brute-force idea.** In a balanced string of brackets, some pair like `()` must
appear side by side. Delete it, and the rest is still balanced. So we can keep deleting
adjacent pairs until nothing changes, and check whether the string became empty.

```{.java include="code-examples/ch10/BalancedParentheses.java" region="erasing"}
```

This is correct, but slow. Each pass scans the whole string, O(n), and a string like
`"((((...))))"` loses only one pair per pass, so there can be about n/2 passes. The total
is O(n²).

**Finding the fast idea.** Look at what the brute force deletes: always a closing bracket
together with the *nearest unclosed* opening bracket before it. So, while scanning from
left to right, we only ever need to know the most recent opening bracket that has not been
closed yet. "The most recent unfinished thing" is exactly what a stack gives us.

::: {.keyidea title="Bracket matching"}
Push every opening bracket. When a closing bracket arrives, the top of the stack must be
the matching opening bracket; pop it. At the end, the stack must be empty.
:::

```{.java include="code-examples/ch10/BalancedParentheses.java" region="isBalanced" #lst-balanced}
```

**Syntax breakdown.**

- `Deque<Character> open` (line 2) is a deque of `Character` objects. Generic types
  cannot hold primitives such as `char`, so Java uses the wrapper class `Character`.
- `open.push(ch)` (line 6) passes a `char` where a `Character` is expected; Java converts
  it automatically. This is called *autoboxing*.
- `open.pop() != matchingOpen(ch)` (line 8) compares a `Character` (from `pop`) with a
  `char` (from `matchingOpen`). Because one side is a primitive, Java *unboxes* the
  `Character` and compares the two characters by value, which is what we want.
- `open.isEmpty() || open.pop() != ...` relies on *short-circuit evaluation*: if the
  stack is empty, `||` already knows the result is `true` and never calls `pop()`, which
  would otherwise throw an exception.
- Lines 18–22 use a *switch expression* (Java 14 and later): `case ')' -> '(';` means "if
  `close` is `')'`, the value of the whole switch is `'('`". The `default` case covers
  `'}'`. Chapter 23 covers switch expressions fully.

**Line by line.** The loop visits every character (line 3). An opening bracket is pushed
(lines 5–6). A closing bracket is checked (lines 7–10): if nothing is open, or the most
recent open bracket is of a different kind, the string is not balanced and the method
returns immediately. Any other character falls through both `if` tests and is ignored
(line 12). After the loop, the string is balanced only if no opening bracket is still
waiting (line 14).

**Dry run.** First the balanced string `"([]{})"`:

| i | ch | Action | Stack (bottom → top) |
|---|----|--------------------------------|------------------|
| 0 | `(` | push | `(` |
| 1 | `[` | push | `( [` |
| 2 | `]` | pop `[`, it matches | `(` |
| 3 | `{` | push | `( {` |
| 4 | `}` | pop `{`, it matches | `(` |
| 5 | `)` | pop `(`, it matches | (empty) |
| end | | stack empty: return `true` | |

: `isBalanced("([]{})")`

Now `"([)]"`: after pushing `(` and `[`, the third character `)` pops `[`, which does not
match, so the method returns `false` at index 2 without reading the rest.

```{.output from="code-examples/ch10/BalancedParentheses"}
```

**Complexity.** Each character is pushed at most once and popped at most once, so the
time is O(n). In the worst case, a string of only opening brackets, all n characters are
on the stack, so the extra space is O(n).

**Edge cases and common mistakes.**

- *Calling `pop()` on an empty stack.* A string that starts with a closing bracket, such
  as `")("`, must return `false`, not crash. The `isEmpty()` test protects line 8.
- *Forgetting the final check.* `"(("` never triggers a mismatch inside the loop; only
  `return open.isEmpty()` catches it.
- *Counting instead of matching.* Counting opens and closes gets `"([)]"` wrong: the
  counts balance, but the order does not. A counter is enough only when there is a single
  bracket type (see the interview follow-ups below).
- *The empty string* is balanced: nothing is left open.

::: {.tip title="Follow-up questions to expect"}
**"What if there is only one kind of bracket?"** Then a single integer `depth` replaces
the stack: add 1 for `(`, subtract 1 for `)`, fail if it ever goes below 0, and require 0
at the end. That uses O(1) extra space. **"Can you report where the error is?"** Push
indices instead of characters; Exercise 10.2 asks for this. **"What if the input is a
stream too large for memory?"** The algorithm reads each character once, left to right,
so it works on a stream; only the stack of open brackets must fit in memory.
:::

## Evaluating expressions with stacks {#expressions}

Stacks are the classic tool for evaluating arithmetic expressions. They also explain how
the JVM itself runs your code: the bytecode instruction `iadd`, for example, pops two
`int` values from an *operand stack* and pushes their sum (Chapter 5).

### Postfix notation {#rpn}

::: {.definition title="Postfix notation"}
In [postfix notation]{.idx}, also called *Reverse Polish notation* (RPN), each operator
comes *after* its two operands. The usual form, `3 + 4`, is called
[infix notation]{.idx}; its postfix form is `3 4 +`. The infix expression `(3 + 4) * 2`
becomes `3 4 + 2 *`.
:::

Postfix needs no parentheses and no precedence rules: the order of the tokens alone says
what to compute. That makes it easy to evaluate with a stack. Read the tokens from left
to right; push every number; when an operator arrives, pop two numbers, apply the
operator, and push the result. (LeetCode 150, *Evaluate Reverse Polish Notation*, is this
problem.)

```{.java include="code-examples/ch10/EvaluateRpn.java" region="evaluate" #lst-rpn}
```

**Syntax breakdown.**

- `for (String token : tokens)` (line 3) is the enhanced `for` loop: it visits each
  element of the array in order.
- `switch (token)` (line 4) switches on a `String`, comparing with `equals`, not `==`.
  `case "+", "-", "*", "/" -> { ... }` (line 5) lists several labels for one branch, and
  the braces allow several statements in an arrow branch.
- `Integer.parseInt(token)` (line 10) converts text such as `"-7"` to the `int` -7. It
  throws `NumberFormatException` for text that is not a number. A token like `"-7"` is a
  number, not the minus operator, because the `case` labels match only the exact string
  `"-"`.
- `int right = operands.pop();` (line 6) unboxes the `Integer` returned by `pop` into an
  `int`.

**The order of operands.** The first value popped is the *right* operand (line 6), and
the second is the *left* operand (line 7). For `+` and `*` the order does not matter, but
`10 3 -` must compute `10 - 3`, not `3 - 10`. Swapping these two lines is the most common
bug in this problem.

**Dry run** of `5 1 2 + 4 * + 3 -`, which is the infix expression `5 + (1 + 2) * 4 - 3`:

| Token | Action | Stack (bottom → top) |
|-------|-------------------------------------|------------------|
| `5` | push 5 | 5 |
| `1` | push 1 | 5 1 |
| `2` | push 2 | 5 1 2 |
| `+` | pop 2 and 1, push 1 + 2 = 3 | 5 3 |
| `4` | push 4 | 5 3 4 |
| `*` | pop 4 and 3, push 3 × 4 = 12 | 5 12 |
| `+` | pop 12 and 5, push 5 + 12 = 17 | 17 |
| `3` | push 3 | 17 3 |
| `-` | pop 3 and 17, push 17 − 3 = 14 | 14 |

: Evaluating 5 1 2 + 4 * + 3 -

```{.output from="code-examples/ch10/EvaluateRpn"}
```

The last two lines show how Java's `int` division works: it rounds toward zero, so
`7 / 2` is 3 and `-7 / 2` is -3, not -4. If a problem needs rounding toward negative
infinity, use `Math.floorDiv`. The method takes O(n) time and O(n) space for n tokens.

### Infix expressions with two stacks {#infix}

Infix expressions need two more ideas: **precedence** (`*` and `/` bind more tightly than
`+` and `-`) and **parentheses**. A classic method, a simplified form of Edsger Dijkstra's
*shunting-yard algorithm*, uses two stacks: one for numbers and one for operators. The
rules are:

1. A number is pushed onto the number stack.
2. `(` is pushed onto the operator stack.
3. `)` applies operators until the matching `(` is on top, then discards the `(`.
4. An operator first applies every operator on the stack that has the same or higher
   precedence (stopping at a `(`), then pushes itself.
5. At the end, apply everything that is left.

"Apply" means: pop an operator and two numbers, compute, and push the result.

```{.java include="code-examples/ch10/InfixCalculator.java" region="evaluate" #lst-infix}
```

**How the code follows the rules.** Lines 9–16 read a whole multi-digit number: each new
digit multiplies the number so far by 10 and adds the digit's value. The expression
`expression.charAt(i) - '0'` turns a digit character into its value, because the
characters `'0'` to `'9'` have consecutive codes. Lines 17–19 implement rule 2, lines
20–25 rule 3, lines 26–32 rule 4, and lines 35–37 rule 5. `applyTop` (lines 45–56) pops
the right operand before the left one, for the same reason as in the postfix evaluator.

**Why "same or higher" precedence?** The `>=` on line 28 makes operators of equal
precedence apply from left to right. For `10 - 4 - 3`, when the second `-` arrives, the
first `-` is applied immediately, giving `(10 - 4) - 3 = 3`. With `>` instead, the code
would compute `10 - (4 - 3) = 9`, which is wrong.

**Dry run** of `8 / (3 - 1) * 5`:

| Read | Numbers | Operators | What happened |
|------|---------|-----------|----------------------------------------------|
| `8` | 8 | | push 8 |
| `/` | 8 | `/` | operator stack empty: push `/` |
| `(` | 8 | `/ (` | push `(` |
| `3` | 8 3 | `/ (` | push 3 |
| `-` | 8 3 | `/ ( -` | top is `(`: just push `-` |
| `1` | 8 3 1 | `/ ( -` | push 1 |
| `)` | 8 2 | `/` | apply `3 - 1 = 2`, discard `(` |
| `*` | 4 | `*` | `/` has equal precedence: apply `8 / 2 = 4`, then push `*` |
| `5` | 4 5 | `*` | push 5 |
| end | 20 | | apply `4 * 5 = 20` |

: Evaluating 8 / (3 - 1) * 5 (stacks shown bottom → top)

```{.output from="code-examples/ch10/InfixCalculator"}
```

Every character is read once, and every number and operator is pushed and popped at most
once, so evaluation is O(n) time and O(n) space.

::: {.warning title="Character.isDigit accepts more than 0-9"}
The code tests `ch >= '0' && ch <= '9'` instead of `Character.isDigit(ch)`. The library
method returns `true` for *every* Unicode decimal digit, including the Bengali digits ০ to
৯. For such a character, `ch - '0'` is a large meaningless number. When you parse input
that should contain only ASCII digits, test the range explicitly.
:::

**Limitations.** This calculator does not handle unary minus (`-3 + 4`), and malformed
input causes exceptions rather than clear error messages. For example, an unmatched `)`
makes `operators.peek()` return `null` on line 21, and comparing `null` with `'('`
throws `NullPointerException` during unboxing. A production parser would validate its
input; in an interview, state these limits out loud and ask whether the input is
guaranteed to be valid.

## Monotonic stacks {#monotonic-stack}

This section introduces one of the most useful patterns in coding interviews. It turns
a family of O(n²) problems into O(n) ones.

**The problem.** For each element of an array, find the *next greater element*: the first
value to its right that is strictly larger. If there is none, the answer is -1. For
`[2, 7, 3, 5, 4, 6, 8]`, the answers are `[7, 8, 5, 6, 6, 8, -1]`.

**Brute force.** For each position, scan to the right until a larger value appears.

```{.java include="code-examples/ch10/NextGreater.java" region="brute"}
```

In the worst case, a decreasing array, every inner loop runs to the end: about n²/2
comparisons, so O(n²).

**Finding the fast idea.** Scan from left to right and keep a list of positions that are
*still waiting* for their answer. Two observations make this list special:

1. When a new value `x` arrives, it is the answer for *every* waiting position whose
   value is smaller than `x`, because `x` is the first larger value to their right.
2. After those positions are answered and removed, every remaining waiting value is at
   least `x`. So the waiting values, from oldest to newest, never increase.

Because the waiting values are in non-increasing order, the smaller ones are always the
most recent ones. So we only ever need to look at, and remove from, the newest end of the
list. That is a stack.

::: {.definition title="Monotonic stack"}
A [monotonic stack]{.idx} is a stack whose elements stay in sorted order from bottom to
top: always increasing, or always decreasing. Before pushing a new element, the algorithm
pops every element that would break the order. The pops are where the useful work happens.
:::

```{.java include="code-examples/ch10/NextGreater.java" region="fast" #lst-nextgreater}
```

**Line by line.** Line 3 fills the result with -1, the answer for any position that never
finds a larger value. The stack `waiting` holds *indices*, not values (line 4), because we
must know *which* position to answer. For each new index `i`, the `while` loop (lines 6–8)
pops every waiting index whose value is smaller than `values[i]` and records `values[i]`
as its answer. Then `i` itself starts waiting (line 9). Indices still on the stack at the
end keep their -1.

**Dry run** on `[2, 7, 3, 5, 4, 6, 8]`. The stack column shows `index:value` pairs.

| i | values[i] | Popped (and answered) | Stack after push (bottom → top) |
|---|-----------|--------------------------|-----------------------------|
| 0 | 2 | none | 0:2 |
| 1 | 7 | 0 (answer 7) | 1:7 |
| 2 | 3 | none | 1:7, 2:3 |
| 3 | 5 | 2 (answer 5) | 1:7, 3:5 |
| 4 | 4 | none | 1:7, 3:5, 4:4 |
| 5 | 6 | 4 (answer 6), 3 (answer 6) | 1:7, 5:6 |
| 6 | 8 | 5 (answer 8), 1 (answer 8) | 6:8 |
| end | | index 6 keeps -1 | |

: Next greater element with a monotonic stack

```{.output from="code-examples/ch10/NextGreater"}
```

**Why it is O(n).** The `while` loop is nested inside the `for` loop, so the code *looks*
quadratic. But count the work per index instead of per loop: each index is pushed exactly
once and popped at most once. So the total number of pops over the whole run is at most
n, and the total time is O(n), with O(n) extra space for the stack. This is an amortized
argument: a single iteration of the `for` loop can pop many indices, but the total over
all iterations is bounded. Interviewers often ask you to explain exactly this.

**The four variants.** The same skeleton answers four related questions. Only the
comparison changes, and whether the answer is recorded for the *popped* index or for the
*current* one:

| Question, for each position i | Pop while the top's value is… | Record the answer |
|-------------------------------------|-------------------------------|--------------------------------|
| next greater (to the right) | `< values[i]` | for each popped index: it is `i` |
| next smaller | `> values[i]` | for each popped index: it is `i` |
| previous greater (to the left) | `<= values[i]` | for `i`: the top after popping |
| previous smaller | `>= values[i]` | for `i`: the top after popping |

: Monotonic stack variants (strict "greater" and "smaller")

Here is the previous-smaller variant, which the next section needs:

```{.java include="code-examples/ch10/NextGreater.java" region="smaller" #lst-prevsmaller}
```

When equal values matter, decide deliberately between `<` and `<=`. The table uses
*strictly* greater and smaller. If a problem asks for "greater than or equal", flip the
equality in the pop condition, and test with repeated values.

### Application: the largest rectangle in a histogram {#histogram}

**The problem.** A histogram has bars of width 1 and heights `heights[0..n-1]`. Find the
area of the largest rectangle that fits entirely under the bars. For heights
`[2, 1, 5, 6, 2, 3]`, the answer is 10: the bars of height 5 and 6 together hold a
rectangle of height 5 and width 2. (This is LeetCode 84, a well-known hard problem.)

```{.diagram caption="Heights [2, 1, 5, 6, 2, 3]; the best rectangle (#) has area 5 × 2 = 10"}
  6                  [  ]
  5             [##] [##]
  4             [##] [##]
  3             [##] [##]      [  ]
  2   [  ]      [##] [##] [  ] [  ]
  1   [  ] [  ] [##] [##] [  ] [  ]
      -----------------------------
index  0    1    2    3    4    5
```

**Brute force.** Try every pair `(left, right)` of bar positions; the tallest rectangle
spanning them has the height of the shortest bar between them.

```{.java include="code-examples/ch10/LargestRectangle.java" region="brute"}
```

This is O(n²): fine for 1,000 bars, too slow for 100,000.

**Finding the fast idea.** Every optimal rectangle has some bar that is the *shortest*
bar inside it, and the rectangle uses that bar's full height. So for each bar `j`, ask:
how far can a rectangle of height `heights[j]` stretch? It stretches left until the
*previous smaller* bar and right until the *next smaller* bar. Its width is
`next - previous - 1`. A monotonic stack with increasing heights gives both limits at
once: when bar `j` is popped because a shorter bar `i` arrived, `i` is its next smaller
bar, and the element just below `j` on the stack is its previous smaller bar.

```{.java include="code-examples/ch10/LargestRectangle.java" region="largest" #lst-histogram}
```

**Line by line.** This version uses a plain `int[]` as the stack (line 3) with `top`
counting its elements (line 4), which avoids boxing every index into an `Integer`. The loop
runs one step *past* the end (line 6); at `i == n` the current height is 0 (line 7), which
is shorter than every bar and so pops everything that remains. Inside the `while` loop,
`stack[--top]` first decreases `top` and then reads the popped index (line 9). The
previous smaller bar is the new top, or -1 if the stack is now empty (line 10). Line 11
computes the width, and line 12 updates the best area. `height * width` multiplies an
`int` by a `long`, so Java computes it as a `long`.

**Dry run** on `[2, 1, 5, 6, 2, 3]` (stack shows indices):

| i | current | Popped: height × width | best | Stack after |
|---|---------|-------------------------------------------|------|-------------|
| 0 | 2 | none | 0 | 0 |
| 1 | 1 | index 0: 2 × (1 − (−1) − 1) = 2 | 2 | 1 |
| 2 | 5 | none | 2 | 1, 2 |
| 3 | 6 | none | 2 | 1, 2, 3 |
| 4 | 2 | index 3: 6 × (4 − 2 − 1) = 6; index 2: 5 × (4 − 1 − 1) = 10 | 10 | 1, 4 |
| 5 | 3 | none | 10 | 1, 4, 5 |
| 6 | 0 | index 5: 3 × 1 = 3; index 4: 2 × 4 = 8; index 1: 1 × 6 = 6 | 10 | 6 |

: Largest rectangle with a monotonic stack

```{.output from="code-examples/ch10/LargestRectangle"}
```

The time is O(n), by the same push-once, pop-once argument, and the extra space is O(n).

::: {.warning title="Overflow in area calculations"}
With up to 10^5^ bars of height up to 10^5^, an area can reach 10^10^, which is far larger
than the largest `int` (about 2.1 × 10^9^). The code stores `best` and `width` as `long`.
Writing `int area = height * (i - leftLimit - 1);` would overflow silently and produce a
wrong, possibly negative, answer. The self-check program tests exactly this case.
:::

::: {.tip title="Recognizing a monotonic-stack problem"}
Look for phrases such as *next greater*, *previous smaller*, *how many days until*,
*span*, *visible to the left*, *nearest larger*, or *remove digits to make the smallest
number*. If the brute force scans outward from each element until it meets something
larger or smaller, a monotonic stack can usually do it in O(n). When you present the
solution, state the invariant ("the stack holds indices whose values decrease from
bottom to top") and the amortized argument ("each index is pushed and popped at most
once").
:::

## Monotonic queues: the sliding-window maximum {#monotonic-queue}

**The problem.** Given an array and a window size `k`, return the maximum of every window
of `k` consecutive elements. For `[1, 3, -1, -3, 5, 3, 6, 7]` and `k = 3`, the windows are
`[1, 3, -1]`, `[3, -1, -3]`, `[-1, -3, 5]`, `[-3, 5, 3]`, `[5, 3, 6]` and `[3, 6, 7]`,
and their maxima are `[3, 3, 5, 5, 6, 7]`. (LeetCode 239.)

**Brute force.** Scan each window: O(k) per window and O(nk) in total. With n = 10^5^ and
k = 5 × 10^4^, that is billions of operations.

```{.java include="code-examples/ch10/SlidingWindowMax.java" region="brute"}
```

A heap (Chapter 15) can do it in O(n log k). A deque does it in O(n).

**Finding the fast idea.** Suppose the window contains an element at index `j` and a
later element at index `i > j` with `values[i] >= values[j]`. Then `j` can *never* be the
maximum of any future window: every window that contains `j` and is still to come also
contains `i`, which is at least as large, and `i` stays in the window longer. So `j` can
be thrown away. After throwing away all such elements, the remaining indices have values
that strictly *decrease* from oldest to newest, and the maximum is the oldest one.

::: {.definition title="Monotonic queue"}
A [monotonic queue]{.idx} is a deque whose elements stay in sorted order from front to
back. New elements enter at the back after removing the elements they dominate; elements
leave from the front when they expire. The front is always the best element of the current
window.
:::

```{.java include="code-examples/ch10/SlidingWindowMax.java" region="fast" #lst-slidingmax}
```

**Line by line.** The method first rejects impossible window sizes (lines 2–4). There are
`n - k + 1` windows, so that is the size of the result (line 5). The deque holds indices
(line 6). For each new index `i`:

- lines 8–10 remove the front index if it has fallen out of the window. The window ending
  at `i` covers indices `i - k + 1` to `i`, so an index `<= i - k` is too old. At most one
  index can expire per step, so an `if` is enough;
- lines 11–13 remove from the *back* every index whose value is at most `values[i]`;
- line 14 adds `i` at the back;
- once the first full window has been seen (`i >= k - 1`, line 15), the front holds the
  maximum of the window ending at `i` (line 16).

**Dry run** with `k = 3` (the deque shows `index:value`, front first):

| i | values[i] | Expired from front | Removed from back | Deque after | Output |
|---|-----------|--------------------|-------------------|-------------|--------|
| 0 | 1 | | | 0:1 | |
| 1 | 3 | | 0:1 | 1:3 | |
| 2 | -1 | | | 1:3, 2:-1 | 3 |
| 3 | -3 | | | 1:3, 2:-1, 3:-3 | 3 |
| 4 | 5 | 1:3 | 3:-3, 2:-1 | 4:5 | 5 |
| 5 | 3 | | | 4:5, 5:3 | 5 |
| 6 | 6 | | 5:3, 4:5 | 6:6 | 6 |
| 7 | 7 | | 6:6 | 7:7 | 7 |

: Sliding-window maximum with a monotonic deque

```{.output from="code-examples/ch10/SlidingWindowMax"}
```

**Complexity.** Each index enters the deque once and leaves at most once, from either end.
So the total work is O(n), and the deque holds at most k indices: O(k) extra space, plus
the output array.

**Common mistakes.**

- *Storing values instead of indices.* With values alone, you cannot tell when the front
  element has left the window.
- *Forgetting the expiry check,* or using `< i - k` instead of `<= i - k`, which keeps an
  element one step too long.
- *Writing the result at index `i`* instead of `i - k + 1`.
- *Using `<` instead of `<=` on line 11.* Both give correct maxima, because an equal,
  newer value is as good as an older one. With `<=`, duplicates are removed earlier and
  the deque stays smaller.

## Queues in breadth-first search (a preview) {#bfs-preview}

Chapter 17 studies graphs in depth. This section shows the reason queues matter there.

**The problem.** A grid has open cells `.` and walls `#`. Starting at the top-left cell
and moving up, down, left or right, what is the smallest number of steps to reach the
bottom-right cell? If it cannot be reached, return -1.

**The idea.** [Breadth-first search]{.idx term="breadth-first search (BFS)"} (BFS)
explores the grid in *rings*: first the start, then every cell 1 step away, then every
cell 2 steps away, then 3, and onward. A queue produces exactly this order. All cells at distance
d enter the queue before any cell at distance d + 1, because each cell at distance d + 1
is discovered while processing a cell at distance d. So the first time BFS reaches a cell,
it has found a shortest path to it.

```{.java include="code-examples/ch10/GridBfs.java" region="bfs" #lst-gridbfs}
```

**Key details.** The `distance` array (lines 7–10) does two jobs: it stores each cell's
distance and, with -1, marks cells that have not been reached. Cells are stored in the
queue as two-element arrays `{row, col}` (line 11); a small record class would also work
(Chapter 3). A neighbour's distance is set *when it is added to the queue* (line 28), not
when it is removed. Marking early guarantees that no cell is added twice. The two arrays
`ROW_STEP` and `COL_STEP`, declared at the top of the class, list the four moves, so a
single loop (lines 21–31) handles all directions.

```{.diagram caption="Distance of each cell from the top-left corner (# is a wall)"}
   grid          distances
   . . # .        0  1  #  7
   . # . .        1  #  5  6
   . . . .        2  3  4  5
   # . # .        #  4  #  6
```

```{.output from="code-examples/ch10/GridBfs"}
```

**Complexity.** Each of the R × C cells enters the queue at most once and is examined
with four neighbours, so BFS takes O(R × C) time and O(R × C) space for the `distance`
array and the queue.

::: {.warning title="Marking cells too late"}
If you mark a cell as visited only when you *remove* it from the queue, the same cell can
be added many times by different neighbours before it is processed. The answer is still
correct, but the queue can grow far beyond R × C and the program may run out of time or
memory on large grids. Mark when you enqueue.
:::

## Building one structure from another {#simulations}

Two classic interview questions ask you to build a queue using only stacks, and a stack
using only queues. They test whether you understand the two orders well enough to convert
one into the other, and the first one is a beautiful example of amortized analysis.

### A queue from two stacks {#queue-two-stacks}

Use one stack, the *inbox*, for new elements and a second stack, the *outbox*, for
elements that are ready to leave. Popping everything from the inbox and pushing it onto
the outbox *reverses* the order, which turns the newest-first order of a stack into the
oldest-first order of a queue.

```{.java include="code-examples/ch10/QueueWithStacks.java" region="queue" kind="Fields and methods" #lst-twostacks}
```

The important rule is on line 29: refill the outbox **only when it is empty**. If the
outbox still holds elements, they are older than everything in the inbox and must leave
first.

```{.output from="code-examples/ch10/QueueWithStacks"}
```

Here is what happened. After `a`, `b` and `c` were enqueued, the inbox held them with `c`
on top. The first `dequeue` found the outbox empty and moved all three, leaving `a` on
top of the outbox, and returned `a`. Then `d` went into the inbox. The next two dequeues
returned `b` and `c` straight from the outbox. The last dequeue found the outbox empty
again, moved `d`, and returned it.

**Cost.** One `dequeue` can be expensive: if n elements are waiting in the inbox, it
moves all n, which is O(n). But follow a single element through its life: it is pushed
onto the inbox once, popped from the inbox once, pushed onto the outbox once, and popped
from the outbox once. That is four stack operations per element, ever. So any sequence of
n queue operations costs O(n) in total, and each operation is O(1) amortized.

### A stack from one queue {#stack-one-queue}

To make a queue behave like a stack, keep the newest element at the *front*. After adding
a new element at the back, move every older element from the front to the back, behind
the new one.

```{.java include="code-examples/ch10/StackWithQueue.java" region="stack" kind="Fields and methods" #lst-onequeue}
```

The loop on lines 5–7 runs `size - 1` times, so `push` is O(n), while `pop` and `peek`
are O(1). The opposite choice (cheap push, expensive pop) is also possible; which one is
better depends on which operation the program performs more often.

```{.output from="code-examples/ch10/StackWithQueue"}
```

## Choosing the right structure {#choosing}

| You need to… | Structure | In Java |
|----------------------------------------------------|-------------------|---------------------------|
| undo the most recent action; match nested things; do DFS without recursion | stack | `Deque<T> s = new ArrayDeque<>()` with `push`, `pop`, `peek` |
| process items in arrival order; do BFS | queue | `Deque<T> q = new ArrayDeque<>()` with `offer`, `poll`, `peek` |
| add or remove at both ends; sliding windows | deque | `ArrayDeque` with the `First` and `Last` methods |
| answer "next/previous greater or smaller" for every element | monotonic stack | `ArrayDeque` of indices, or an `int[]` with a `top` counter |
| keep the max or min of a moving window | monotonic queue | `ArrayDeque` of indices |
| always take the smallest or largest item, not the oldest | priority queue | `PriorityQueue` (Chapter 15) |
| a stack of primitives in performance-critical code | array stack | `int[]` plus a `top` index |

: Which structure to use

**Mistakes to avoid in every stack or queue problem.**

- Calling `pop`, `remove` or `element` on a possibly empty deque. Check `isEmpty()`
  first, or use `poll`/`peek` and handle `null`.
- Mixing up ends: using `push` (front) to add and `pollLast` (back) to remove turns a
  stack into a queue by accident.
- Comparing `Integer` values from a deque with `==`.
- Storing values when the algorithm needs indices (to compute distances or detect
  expiry).
- Assuming a `while` loop inside a `for` loop must be O(n²); count pushes and pops
  instead.

## Summary {#summary}

Stacks, queues and deques decide *which waiting element comes next*: the newest (stack),
the oldest (queue), or either end (deque). Each can be built on a growable array with O(1)
amortized operations, or on linked nodes with O(1) worst-case operations but more memory.
In Java, `ArrayDeque` is the default choice for all three roles; the legacy `Stack` class
should be avoided. Stacks solve bracket matching and expression evaluation, and the
*monotonic stack* and *monotonic queue* patterns solve "next greater", "previous smaller"
and "sliding-window maximum" problems in linear time, because every element enters and
leaves the structure at most once. A queue makes breadth-first search find shortest paths
in unweighted grids and graphs.

| Idea | Remember |
|------------------------------|----------------------------------------------------------|
| Stack | LIFO; `push`, `pop`, `peek` on the front of an `ArrayDeque` |
| Queue | FIFO; `offer` at the back, `poll` and `peek` at the front |
| Circular array | indices wrap with `% capacity`; keep `size` to tell full from empty |
| Doubling | makes array growth O(1) amortized per insertion |
| `ArrayDeque` | no `null`s; `peek`/`poll` return `null` when empty, `pop`/`remove` throw |
| Monotonic stack | pop what the new element "beats"; each index pushed and popped once: O(n) |
| Monotonic queue | drop dominated elements at the back and expired ones at the front |
| BFS | a queue visits cells in order of distance; mark when you enqueue |
| Queue from two stacks | refill the outbox only when it is empty; O(1) amortized |

: Key takeaways

## Exercises {#exercises}

::: {.exercise number="10.1" level="Easy" title="Predict the output"}
Without running it, predict what this fragment prints. Then explain each step in terms of
the *front* and *back* of the deque.

```{.fragment}
Deque<Integer> d = new ArrayDeque<>();
d.push(1);
d.offer(2);
d.push(3);
d.offerFirst(4);
d.addLast(5);
System.out.println(d);
System.out.println(d.pop() + " " + d.pollLast() + " " + d.peek());
```
:::

::: {.exercise number="10.2" level="Easy" title="Where is the error?"}
Write `int firstError(String text)`. It ignores characters other than the six brackets.
If some closing bracket has no matching opening bracket, return its index. Otherwise, if
some opening bracket is never closed, return the index of the earliest such bracket. If
the brackets are balanced, return -1. For example, `"x(y]z"` gives 3 and `"((a)"` gives 0.
:::

::: {.exercise number="10.3" level="Easy" title="A fixed-capacity circular queue"}
Implement a class `CircularQueue` with a constructor taking the capacity, and methods
`boolean enqueue(int value)` (returns `false` if the queue is full),
`boolean dequeue()` (returns `false` if it is empty), `int front()` and `int rear()`
(each returns -1 if the queue is empty), `isEmpty()` and `isFull()`. Every method must be
O(1), and the queue must never grow.
:::

::: {.exercise number="10.4" level="Medium" title="Min stack"}
Design a stack of `int` values that supports `push`, `pop`, `top` and `getMin`, where
`getMin` returns the smallest value currently on the stack. All four operations must run
in O(1) time.
:::

::: {.exercise number="10.5" level="Medium" title="Days until a warmer day"}
Given daily temperatures, return an array where element `i` is the number of days after
day `i` until a strictly warmer temperature, or 0 if there is none. For
`[30, 32, 31, 29, 33, 33, 28]` the answer is `[1, 3, 2, 1, 0, 0, 0]`. Aim for O(n).
:::

::: {.exercise number="10.6" level="Medium" title="Stock span"}
Write a class with a method `int next(int price)` that is called once per day with that
day's stock price. It returns the *span*: the number of consecutive days, ending today,
on which the price was less than or equal to today's price. For the prices 100, 80, 60,
70, 60, 75, 85 the spans are 1, 1, 1, 2, 1, 4, 6. Each call should take O(1) amortized
time.
:::

::: {.exercise number="10.7" level="Medium" title="Simplify a path"}
Given an absolute Unix-style path, return its simplest form. A single `.` means the
current folder, `..` means the parent folder (at the root it stays at the root), and
several slashes in a row count as one. The result starts with one `/`, has single slashes
between folder names, and has no trailing slash. For example, `"/a/./b/../../c/"` becomes
`"/c"`, and `"/..."` stays `"/..."` because `...` is an ordinary folder name.
:::

::: {.exercise number="10.8" level="Medium" title="Decode a string"}
An encoded string uses the rule `k[text]`, meaning `text` repeated `k` times; rules can
be nested. Decode it. For example, `"3[a2[c]]"` becomes `"accaccacc"` and
`"2[abc]3[cd]ef"` becomes `"abcabccdcdcdef"`. Assume the input is valid, counts are
positive and may have several digits, and letters are lowercase.
:::

::: {.exercise number="10.9" level="Medium" title="Asteroid collision"}
Asteroids move along a line. A positive value moves right and a negative value moves
left; the absolute value is the asteroid's size. When two asteroids meet, the smaller
one explodes, and if they are the same size, both explode. Asteroids moving in the same
direction never meet. Return the asteroids that remain. For example, `[5, 10, -5]`
becomes `[5, 10]` and `[10, 2, -5]` becomes `[10]`.
:::

::: {.exercise number="10.10" level="Medium" title="First negative number in every window"}
Given an array and a window size `k`, return, for every window of `k` consecutive
elements, the first negative number in that window, or 0 if the window has none. For
`[12, -1, -7, 8, -15, 30, 16, 28]` and `k = 3`, the answer is `[-1, -1, -7, -15, -15, 0]`.
Aim for O(n).
:::

::: {.exercise number="10.11" level="Hard" title="Remove k digits"}
A non-negative integer is given as a string of digits. Remove exactly `k` digits so that
the remaining number is as small as possible, and return it without leading zeros (return
`"0"` if nothing is left). For example, `"1432219"` with `k = 3` gives `"1219"`, and
`"10200"` with `k = 1` gives `"200"`. Explain why your greedy choice is correct.
:::

::: {.exercise number="10.12" level="Hard" title="Sum of subarray minimums"}
Given an array of n integers with 1 ≤ n ≤ 30,000 and each value between 1 and 30,000,
return the sum of `min(subarray)` over all contiguous subarrays, modulo 1,000,000,007.
For `[3, 1, 2, 4]` the answer is 17. Aim for O(n), and make sure equal values are not
counted twice.
:::

::: {.exercise number="10.13" level="Medium" title="Rotting oranges"}
In a grid, 0 is an empty cell, 1 is a fresh orange and 2 is a rotten orange. Every
minute, each rotten orange makes its fresh neighbours (up, down, left, right) rotten.
Return the number of minutes until no fresh orange remains, or -1 if some fresh orange
can never rot. A grid with no fresh oranges needs 0 minutes.
:::

::: {.exercise number="10.14" level="Challenge" title="Maximal rectangle in a binary matrix"}
Given a matrix of `'0'` and `'1'` characters, find the area of the largest rectangle that
contains only `'1'`s. Hint: for each row, how tall is the column of consecutive `'1'`s
ending at that row in each column? Reuse an algorithm from this chapter. Aim for
O(rows × cols).
:::

::: {.exercise number="10.15" level="Medium" title="Amortized cost, carefully"}
For the queue built from two stacks: (a) give a sequence of operations in which a single
`dequeue` performs n stack pops and n stack pushes; (b) prove that any sequence of m
queue operations, starting from an empty queue, performs at most 4m stack operations
in total, not counting the `isEmpty` checks.
:::

## Solutions {#solutions}

### Warm-up quiz answers {#quiz-answers}

1. **3**, because the last value pushed is the first popped (LIFO).
2. **1**, because the first value added is the first removed (FIFO).
3. `peek()` returns `null`; `pop()` throws `NoSuchElementException`.
4. A single push can cost O(n) when it triggers a resize, but over any sequence of n
   pushes the total work is O(n), so the *average* per push is O(1). "Amortized"
   describes this average over a sequence, not the cost of each individual operation.
5. Typical answers: matching brackets or HTML tags, undo, the back button, evaluating
   nested expressions, and returning from nested method calls.

::: {.solution number="10.1"}
The deque changes as follows (front on the left): `push(1)` adds at the front: `[1]`.
`offer(2)` adds at the back: `[1, 2]`. `push(3)` adds at the front: `[3, 1, 2]`.
`offerFirst(4)` adds at the front: `[4, 3, 1, 2]`. `addLast(5)` adds at the back:
`[4, 3, 1, 2, 5]`. So the first line prints `[4, 3, 1, 2, 5]`. Then `pop()` removes the
front, 4; `pollLast()` removes the back, 5; `peek()` looks at the new front, 3. The
second line prints `4 5 3`. The file `solutions/ch10/DequeTrace.java` runs the fragment:

```{.output from="solutions/ch10/DequeTrace"}
```

The lesson: `push` and `offer` work on *different ends*. Mixing them on one deque is
legal but almost always a bug.
:::

::: {.solution number="10.2"}
Push *indices* of opening brackets instead of the brackets themselves; the character can
always be read back from the string. A plain `int[]` works as the stack because there
can never be more than `text.length()` open brackets.

```{.java include="solutions/ch10/FirstBracketError.java" region="solve"}
```

A mismatched closing bracket is reported as soon as it is found. If the loop finishes
with brackets still open, the earliest unclosed one is at the *bottom* of the stack,
`openIndex[0]`, because the stack keeps open brackets in the order they appeared.

```{.output from="solutions/ch10/FirstBracketError"}
```

Time O(n), extra space O(n).
:::

::: {.solution number="10.3"}
This is `IntQueue` without growth. Because the capacity is fixed, `enqueue` reports a
full queue instead of resizing. The back element sits `size - 1` places after `head`.

```{.java include="solutions/ch10/CircularQueue.java" region="solve" kind="Fields and methods"}
```

```{.output from="solutions/ch10/CircularQueue"}
```

Keeping `size` makes `isEmpty` and `isFull` trivial. Without it, `head == tail` would be
ambiguous, as explained in the warning in []{.ref to="circular-queue"}.
:::

::: {.solution number="10.4"}
Store, with every value, the minimum of the stack *at the moment that value was pushed*.
The minimum of the whole stack is then simply the stored minimum of the top entry, and
popping automatically restores the previous minimum.

```{.java include="solutions/ch10/MinStack.java" region="solve" kind="Fields and methods"}
```

```{.output from="solutions/ch10/MinStack"}
```

All four operations are O(1), and the space is O(n). A common alternative keeps two
stacks: the values, and a second stack that receives a value only when it is less than or
equal to the current minimum. That version must compare the popped value with the top of
the minimum stack, and if both are `Integer` objects, comparing them with `==` is the
classic bug from the warning in []{.ref to="legacy-stack"}. Use `equals`, or unbox to
`int` first.
:::

::: {.solution number="10.5"}
This is "next greater element", but the answer is a *distance* rather than a value, so
the stack must hold indices. The stack keeps days whose temperatures decrease from bottom
to top; a warmer day pops and answers every cooler day still waiting.

```{.java include="solutions/ch10/DailyTemperatures.java" region="solve"}
```

```{.output from="solutions/ch10/DailyTemperatures"}
```

Each day is pushed and popped at most once: O(n) time and O(n) space. Day 4 (33 degrees)
and day 5 (also 33) both get 0, because the comparison is strictly *warmer*: equal
temperatures do not pop each other.
:::

::: {.solution number="10.6"}
Keep a stack of `{price, span}` pairs with prices decreasing from bottom to top. When a
new price arrives, every pair with a price less than or equal to it lies inside the new
span, together with all the days *that* pair already covered. So pop those pairs and add
their spans.

```{.java include="solutions/ch10/StockSpan.java" region="solve" kind="Fields and methods"}
```

```{.output from="solutions/ch10/StockSpan"}
```

On day 6 (price 85), the stack holds the pairs (100, 1), (80, 1), (75, 4), with 75 on
top. The new price pops (75, 4) and (80, 1), so the span is 1 + 4 + 1 = 6. Each pair is
pushed once and popped at most once, so a sequence of n calls takes O(n) time: O(1)
amortized per call.
:::

::: {.solution number="10.7"}
Split the path on `/`. The pieces are folder names, `.`, `..`, or empty strings (from
`//` and from the leading and trailing slashes). Folder names are pushed; `..` pops if
the stack is not empty.

```{.java include="solutions/ch10/SimplifyPath.java" region="solve"}
```

```{.output from="solutions/ch10/SimplifyPath"}
```

Two details deserve attention. `pollFirst()` returns `null` on an empty deque instead of
throwing, which handles `..` at the root for free. And the result must list folders from
the *bottom* of the stack upwards, so the loop takes them with `pollLast()`. Time and
space are O(n) for a path of length n.
:::

::: {.solution number="10.8"}
Each `[` starts a new, nested piece of text. Save the text built so far and the repeat
count on two stacks, and start a fresh builder. At the matching `]`, the finished inner
text is repeated and appended to the saved outer text.

```{.java include="solutions/ch10/DecodeString.java" region="solve"}
```

```{.output from="solutions/ch10/DecodeString"}
```

`String.repeat(n)` (Java 11 and later) returns the string repeated `n` times. The running
time is proportional to the length of the *output*, which can be much longer than the
input: `"9[9[9[a]]]"` is 10 characters but decodes to 729.
:::

::: {.solution number="10.9"}
Only one situation causes a collision: an asteroid moving left meets the nearest
surviving asteroid to its left that is moving right. Keep the survivors on a stack. While
the incoming asteroid moves left and the top moves right, they collide; the loop ends when
the incoming asteroid explodes, or nothing on the stack can hit it.

```{.java include="solutions/ch10/AsteroidCollision.java" region="solve"}
```

```{.output from="solutions/ch10/AsteroidCollision"}
```

Each asteroid is pushed at most once and popped at most once: O(n) time and O(n) space.
The last sample shows the "no collision" case: everything moving left is already to the
left of everything moving right.
:::

::: {.solution number="10.10"}
Only negative numbers can be answers, and among those in the window, the *earliest* one
is the answer. So keep the indices of negative numbers in a queue, in order. Remove the
front when it leaves the window; the front is then the answer.

```{.java include="solutions/ch10/FirstNegativeInWindow.java" region="solve"}
```

```{.output from="solutions/ch10/FirstNegativeInWindow"}
```

O(n) time and O(k) extra space. Unlike the sliding-window maximum, nothing is ever
removed from the back: every negative number stays a candidate until it expires.
:::

::: {.solution number="10.11"}
Compare two numbers of the same length digit by digit from the left: the first different
digit decides. So if some digit is followed by a *smaller* digit, deleting the larger one
lets the smaller digit move one place left, and that makes the result smaller. Removing
any digit to the right instead can never make up for it. This greedy rule leads to a
monotonic stack whose digits never decrease:

```{.java include="solutions/ch10/RemoveKDigits.java" region="solve"}
```

```{.output from="solutions/ch10/RemoveKDigits"}
```

If the digits are already non-decreasing (as in `"112"`), no pops happen and the
remaining removals are taken from the end, where the largest digits are. Leading zeros are
skipped at the end, and an empty result becomes `"0"`. Each digit is appended once and
deleted at most once, so the method is O(n). The self-check program compares it with a
brute force that tries every possible set of digits to keep.
:::

::: {.solution number="10.12"}
Instead of looking at every subarray, ask, for each element, *in how many subarrays is it
the minimum?* If `values[i]` is the minimum, the subarray can start anywhere after the
previous smaller element and end anywhere before the next smaller element. With
`left[i]` and `right[i]` as those boundaries, the count is
`(i - left[i]) × (right[i] - i)`, and the answer is the sum of
`values[i] × count` over all i.

Equal values need care. In `[2, 2]`, the whole array has two equal minimums, but it must
be counted once. The fix is to make the boundaries asymmetric: `left` uses the previous
*strictly smaller* value and `right` uses the next *smaller or equal* value. Then each
subarray is assigned to exactly one of its equal minimums, the leftmost one.

```{.java include="solutions/ch10/SumOfSubarrayMinimums.java" region="solve"}
```

```{.output from="solutions/ch10/SumOfSubarrayMinimums"}
```

The product `values[i] × count` can exceed the range of `int` (here it can reach about
3 × 10^4^ × 2.25 × 10^8^ ≈ 6.75 × 10^12^), so the code computes in `long` and reduces both
factors modulo 1,000,000,007 before multiplying. The product of two numbers below 10^9^ + 7
fits safely in a `long`. Two monotonic-stack passes and one summing pass give O(n) time.
:::

::: {.solution number="10.13"}
This is breadth-first search with *many* starting points: put every rotten orange in the
queue at the start. Process the queue one level at a time; each level is one minute. The
variable `levelSize` records how many oranges rotted during the previous minute.

```{.java include="solutions/ch10/RottingOranges.java" region="solve"}
```

```{.output from="solutions/ch10/RottingOranges"}
```

The loop stops as soon as no fresh oranges remain, so it does not count an extra, empty
minute at the end. Counting `fresh` in advance makes the final check O(1). Time and space
are O(rows × cols). The method copies the grid first, because changing an input array
that the caller still owns is a side effect that callers do not expect.
:::

::: {.solution number="10.14"}
Process the matrix row by row. For each column, `heights[c]` is the number of
consecutive `'1'`s ending at the current row; a `'0'` resets it to 0. Every all-ones
rectangle has a bottom row, and when that row is processed, the rectangle fits under the
histogram `heights`. So the answer is the largest histogram rectangle over all rows.

```{.java include="solutions/ch10/MaximalRectangle.java" region="solve"}
```

```{.output from="solutions/ch10/MaximalRectangle"}
```

Each row costs O(cols) to update the heights and O(cols) for the histogram algorithm, so
the total is O(rows × cols) time and O(cols) extra space.
:::

::: {.solution number="10.15"}
**(a)** Enqueue n elements; they all sit in the inbox. The first `dequeue` finds the
outbox empty and moves all n: n pops from the inbox and n pushes onto the outbox, plus one
final pop.

**(b)** Charge every stack operation to the element it moves. An element is pushed onto
the inbox once, when it is enqueued. It can be moved to the outbox at most once, because
nothing ever moves from the outbox back to the inbox: one pop and one push. It is popped
from the outbox at most once, when it is dequeued. So each element causes at most four
stack operations in its whole life. A sequence of m queue operations enqueues at most m
elements, so it performs at most 4m stack operations. A `peek` that triggers a refill
moves elements that are charged in the same way, so it does not change the bound. Hence
the total cost is O(m), and each operation is O(1) amortized, even though a single
operation can cost O(n).
:::
