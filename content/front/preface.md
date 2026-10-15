# Preface

This book is for a computer science student who already writes Java and wants to use it
well: to solve data structure and algorithm (DSA) problems, to pass online assessments and
technical interviews, and to start building real backend software. It was written with the
Bangladesh job market of 2026 in mind, but almost everything in it applies to software
interviews anywhere.

## What this book assumes

You already know the fundamentals of Java: variables and types, operators, `if` and
`switch`, loops, methods, arrays, `String`, classes and objects, constructors, inheritance,
encapsulation, polymorphism, abstraction, interfaces, and basic exception handling. The
book does not teach these again. When an early chapter returns to a familiar feature, such
as `==` or `String`, it does so to show the traps, the internal behavior, and the way the
feature is used in problem solving.

## What this book tries to do differently

Most interview material shows you a finished solution. This book tries to show you how a
solution is *found*. For every important algorithm you will see:

- the problem it solves and why the obvious approach is too slow;
- the idea that leads to the faster approach, and why that idea is correct;
- a complete Java program that was compiled and run while this book was built;
- a hand trace (a *dry run*) that shows every intermediate state;
- the time and space cost, with the reasoning behind it;
- the mistakes people usually make, and how to avoid them;
- the questions an interviewer is likely to ask next.

The goal is that you can write such solutions yourself, debug them when they fail, and
explain your decisions clearly to another engineer.

## Java version

The book uses **Java 21**, a long-term support (LTS) release, as its baseline. Every
program compiles with `javac --release 21`. **Java 25**, the next LTS release, is
mentioned where it adds something worth knowing, and such material is always labelled.
Online judges sometimes run older versions of Java, so the competitive-programming
templates avoid newer features and state the version they need.

## A note on accuracy

Technical claims are checked against official documentation wherever possible, and the
sources are listed in the References at the end of the book. The book separates what the
Java specification *guarantees* from what the current OpenJDK implementation *happens to
do*, because interviewers sometimes ask about the difference.

The chapters about jobs in Bangladesh separate verified observations (with a source and the
date it was checked) from general advice. Nothing in this book can promise you a job, and
no fixed schedule guarantees mastery. What the book can do is help you prepare well and
measure your own progress honestly.
