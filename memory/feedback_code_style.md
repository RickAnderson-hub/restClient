---
name: code-style-rules
description: Code style rules the user has stated explicitly — enforce in all Java source files
metadata:
  type: feedback
---

1. **Field injection over constructor injection.** Use `@Autowired` on fields, not constructor parameters. Mockito tests use `@InjectMocks` to inject mocks into fields.
   **Why:** User preference stated directly.
   **How to apply:** All Spring `@Service`, `@Component`, `@Repository` classes use `@Autowired` field injection. Test classes use `@Mock` + `@InjectMocks`, never manually call `new ServiceClass(repo1, repo2)`.

2. **No empty lines within method bodies.** Blank lines are allowed between class members (fields, constructors, methods) but not between statements inside a method body.
   **Why:** User stated "no empty lines within methods".
   **How to apply:** Every line inside a `{...}` method body must be a statement or a comment. Remove all blank lines inside methods before committing.

3. **Never use FQCN.** All types must be referenced by simple name, with a corresponding `import` statement. No inline fully-qualified class names anywhere in the code.
   **Why:** User stated "never use FQCN".
   **How to apply:** Add the import, use the short name. Exception: static imports for constants are fine. Check all files for `some.package.SomeClass` patterns in code bodies.

4. **Modern Java idioms.** Use var, records, streams, switch expressions, text blocks, pattern matching instanceof, sealed types, and other modern Java features where they improve clarity.
   **Why:** User stated "use modern java idioms".
   **How to apply:** Prefer `var` for local variable inference, streams over loops, switch expressions over switch statements, pattern matching `instanceof` casts, `Stream.toList()` over `.collect(Collectors.toList())`, etc. Don't use pre-Java-17 patterns when a cleaner modern equivalent exists.

5. **Extensive Javadocs on all production code.** Add Javadoc comments to all public classes, interfaces, and methods. Explain the *why* and any non-obvious constraints, not just what the signature already says. Test classes do not require Javadocs (method names are self-documenting there).
   **Why:** User stated "use javadocs extensively".
   **How to apply:** Every public class and every public/protected method in production code gets a Javadoc. Private methods get one when the logic is non-obvious.

6. **AAA pattern for unit tests.** Structure every test with `// Arrange`, `// Act`, `// Assert` comments as section markers (these also serve as the visual separators since blank lines within methods are forbidden).
   **Why:** User stated "use the AAA way of unit testing".
   **How to apply:** Every test method has exactly three comment markers in order. Arrange sets up state/mocks, Act calls the method under test, Assert verifies outcomes.
