# Hack Virtual Machine: VM to Jack

## Parses one or more Jack (.jack) files in a given directory. Outputs tokenized Virtual Machine (.vm) file(s) to the same location ready to run on the Hack Virtual Machine.

This personal project has been created as an accompaniment for Chapters 10/11 of the book [The Elements of Computing Systems](https://www.nand2tetris.org/course). The aforementioned should be referenced for the full specification of the Hack VM and Jack languages. The Hack VM code is designed to run on the Hack Hardware platform.

An overview of the project, classes and features is described below.

---

# VM Compiler

#### The `VMCompiler` class serves as the software entry point.

Loads Jack source file(s) and initiates the tokenizer which handles file parsing/writing using the inbuilt compilation engine.

# Packages

## `tokenizer`

Handles parsing/writing files. The Tokenizer accepts input files from the VM Compiler. This module marches through each file, sorting lines of Jack code into individual tokens while checking against the Jack language grammar. Virtual Machine language is output to the same directory with matching filename(s) to the input files.

## `tokenlib`:

Library of all constant tokens in the Jack language as well as unique string and integer constants encountered by the Tokenizer.

Full specification of the Jack language and tokens [here](https://www.nand2tetris.org/course).

## `token`

Classes for the token types described above along with helper methods.

## `compilationengine`

The main driver for language grammar matching. The inbuilt `parseToken` method is called once for every token encountered by the Tokenizer.

## `compilationengine/Compile`

Set of inherited classes to handle Jack subroutines (statements, methods, declarations etc). With `CompileClass` as the entry point.

`handleRoutine` methods advance `activeToken`s through their routines until complete; recursively calling subroutines until the entire programme has been parsed. Either an empty string or VM code is returned by the `buildCommand` helper methods depending on the programme position.

## `compilationengine/symboltable`

Manages variables declared in the Jack programme. The compilation engine uses two static tables:

- `classSymbolTable`: `static` and `field` variables visible to the entire Jack class
- `scopedSymbolTable`: `argument` and `var` variables visible only to functions/methods; wiped clean on each subroutine declaration

## `compilationengine/tokenpasser`

Class to match the `activeToken` to the prescribed Jack grammar. Stores an `activeCriteria` variable which can be referenced should an error be thrown, helping the developer debug their programme by referencing the grammar expected from the programme.

## `compilationengine/vmwriter`

Static functions to parse tokens into VM code.

## `errormessage`

Composes formatted error messages/symbol tables to print to the console should compilation fail.

# Loading files

#### The `loadfiles` package contains a utility class for importing files of a specific type within a given directory.

Files can be parsed recursively by setting the `recursive` param to `true` and pointing the function to the base of a folder. This is useful for running all tests in the `/tests` directory.

# Tests

 Jack VM files with output expected from the compiler are stored in `/tests`.

A `recursive` parameter is available on the `loadFiles` function and is helpful for parsing all files in the `/tests` directory. This is used while developing to check the diffs on the test files should any changes modify existing code.

- `/programme-tests`
Jack Programmes from project 11 of [The Elements of Computing Systems](https://www.nand2tetris.org/course). Files with the suffix `.expected.vm` can be compared against the output of this compiler. VM files can also be tested on the supplied [VM Emulator](https://www.nand2tetris.org/software).


- `/jack-langauge`:
Segmented tests to compare VM output for the Jack Grammar.
