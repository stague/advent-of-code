[![CI Build](https://github.com/elwaxoro/advent-of-code/actions/workflows/gradle.yml/badge.svg)](https://github.com/elwaxoro/advent-of-code/actions/workflows/gradle.yml)

# advent-of-code
Just having some fun with https://adventofcode.com/ challenges, solved with Kotlin

## How to has?
Extend `PuzzleDayTester` for each new day, override puzzle1 and puzzle2 functions as you write them

```
class Dec01 : PuzzleDayTester(1, 2020) {
    override fun puzzle1(): Any = "Yay"
    override fun puzzle2(): Any = "Boo"
}
```

By default, these tests fail till they're implemented.
Since the test doesn't know the right answer, check the log output for what to copy into the advent of code test boxes

## Input files
Input files should go in main/resources based on year and date

PuzzleDay has a `load` function to help find them and parse them in a simple way (one string per newline by default)

```
load(testNum: Int? = null, delimiter: String = "\n"): List<String>
```

if `testNum` is given, a file with the format `Dec01-test-1.txt` is looked for instead of `Dec01.txt`
