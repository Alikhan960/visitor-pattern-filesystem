# Visitor Pattern – File System Visitor

Assignment #6 – Software Design Patterns (Astana IT University). Java 17, no external dependencies.

A small file system (`File`, `Directory`) on which different operations are performed by
different visitors **without modifying the element classes**.

## Structure

```
src/main/java/kz/astanait/patterns/visitor/
├── element/
│   ├── FileSystemElement.java      Element interface: accept(visitor)
│   ├── File.java                   Concrete element (leaf, immutable)
│   └── Directory.java              Concrete element (composite)
├── visitor/
│   ├── FileSystemVisitor.java      Visitor interface: visitFile / visitDirectory
│   ├── SizeCalculatorVisitor.java  Operation 1: total size
│   ├── SearchVisitor.java          Operation 2: search by criterion
│   ├── FileCriteria.java           Ready-made search predicates
│   └── TreePrinterVisitor.java     Operation 3 (added later, elements untouched)
├── util/ByteFormat.java
└── Main.java                       Client / demo
src/test/java/.../VisitorSelfTest.java   Dependency-free tests
docs/                                    UML (uml.mmd / uml.png), sample output
```

## Double dispatch in one picture

```java
project.accept(visitor);          // 1st dispatch: runtime type of the ELEMENT (Directory)
  -> visitor.visitDirectory(this) // 2nd dispatch: runtime type of the VISITOR (e.g. SearchVisitor)
```

## Build, test, run

```bash
mkdir -p out
javac --release 17 -d out $(find src -name '*.java')
java -cp out kz.astanait.patterns.visitor.VisitorSelfTest
java -cp out kz.astanait.patterns.visitor.Main
```

IntelliJ IDEA: open the folder, mark `src/main/java` and `src/test/java` as source roots, run `Main`.

## Sample output

See [docs/sample-output.txt](docs/sample-output.txt).

## Adding a new operation

Create a class implementing `FileSystemVisitor` – `File` and `Directory` do not change
(see `TreePrinterVisitor`, added in a separate commit).
