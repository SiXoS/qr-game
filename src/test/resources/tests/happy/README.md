# Happy tests

Tests consist of a .qg file with code and an .out file with expected result.

The result is retrieved from the variable in an assignment in the first line of the init block.
All test codes should therefore have:

```
init {
    out = 0
    // can but other code here
}

// rest of test code
```