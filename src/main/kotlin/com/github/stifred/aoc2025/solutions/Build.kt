package com.github.stifred.aoc2025.solutions

fun <T : Any> parseInput(day: Int, year: Int = 2025, benchmark: Boolean = false, transform: (String) -> T): T {
  val input = loadPuzzleInput(year = year, day = day)

  val before = System.nanoTime()
  val first = transform(input)
  val after = System.nanoTime()

  val count = if (benchmark) benchCount(before, after) else 1
  repeat(count - 1) {
    transform(input)
  }

  val finalAfter = if (!benchmark) after else System.nanoTime()
  val each = (finalAfter - before) / count

  println("\nPARSER (year=$year, day=$day, benchmark=$benchmark, runs=$count)")
  println("Time: ${formatNanos(each)}")

  return first
}

fun <T : Any> solve(part: Int, benchmark: Boolean = false, action: () -> T): T {
  val before = System.nanoTime()
  val first = action()
  val after = System.nanoTime()

  val count = if (benchmark) benchCount(before, after) else 1
  repeat(count - 1) {
    action()
  }

  val finalAfter = if (!benchmark) after else System.nanoTime()
  val each = (finalAfter - before) / count

  println("\nPART $part (benchmark=$benchmark, runs=$count)")
  println("Output: $first")
  println("Time:   ${formatNanos(each)}")

  return first
}

private fun benchCount(before: Long, after: Long): Int {
  val diff = after - before

  return when {
    diff > 10_000_000_000 -> 1
    diff > 100_000_000 -> 10
    diff > 1_000_000 -> 100
    else -> 1_000
  }
}

private fun formatNanos(nanos: Long): String {
  return when {
    nanos > 10_000_000_000 -> "${nanos / 1_000_000_000} s"
    nanos > 10_000_000 -> "${nanos / 1_000_000} ms"
    nanos > 10_000 -> "${nanos / 1_000} µs"
    else -> "$nanos ns"
  }
}
