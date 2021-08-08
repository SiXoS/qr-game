# Contributing

PR's and bug reports are very welcome.

## General guidelines

Use the default IntelliJ Java formatting, including:

1. Spaces for indentation
2. 4 space indentation
3. 8 space continuation indent

Always add tests for the added functionality, preferably 100% coverage.

Update /doc with the new feature.

## Changes that are likely to be approved

1. New functions/classes for general utilities.
2. Performance improvements in the runtime.
3. Bug fixes (backwards compatibility might be a discussion).

## Changes that you'll need strong arguments for

1. New byte code operator. (If we pass 32 operators it will increase the bytecode a lot)
2. Backwards-incompatible changes. (Changes in the language are more likely to be merged than changes in the bytecode)
3. Unnecessary performance improvements that reduce readability. (Mainly applies to non-runtime code)
