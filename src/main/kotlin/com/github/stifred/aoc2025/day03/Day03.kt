package com.github.stifred.aoc2025.day03

import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val banks = parseInput(day = 3) { it.asBanks() }

  solve(part = 1, benchmark = false) { banks.totalJoltage(with = 2) }
  solve(part = 2, benchmark = false) { banks.totalJoltage(with = 12) }
}

fun Collection<List<Int>>.totalJoltage(with: Int) = sumOf { it.joltage(with) }

fun List<Int>.joltage(with: Int): Long = sequence {
  var remaining = this@joltage
  for (i in (0..<with).reversed()) {
    val best = remaining.subList(0, remaining.size - i).max()
    remaining = remaining.subList(remaining.indexOf(best) + 1, remaining.size)

    yield(best)
  }
}.fold(0L) { sum, b -> (sum * 10) + b }

fun String.asBanks() = nonEmptyLineSequence().map { it.asBank() }.toList()
private fun String.asBank() = chunked(1).map { it.toInt() }
