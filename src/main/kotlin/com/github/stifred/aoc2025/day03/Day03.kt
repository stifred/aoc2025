package com.github.stifred.aoc2025.day03

import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val banks = parseInput(day = 3) { it.asBanks() }

  solve(part = 1, benchmark = false) { banks.totalJoltage(with = 2) }
  solve(part = 2, benchmark = false) { banks.totalJoltage(with = 12) }
}

fun Collection<BatteryBank>.totalJoltage(with: Int) = sumOf { it.joltage(with) }

data class BatteryBank(val batteries: List<Int>) {
  fun joltage(with: Int): Long {
    var total = 0L
    var firstIndex = 0
    for (i in (0..<with).reversed()) {
      val subList = batteries.subList(firstIndex, batteries.size - i)
      val first = subList.max()
      firstIndex += subList.indexOf(first) + 1

      total *= 10
      total += first
    }

    return total
  }
}

fun String.asBanks() = nonEmptyLineSequence().map { it.asBank() }.toList()
private fun String.asBank() = BatteryBank(batteries = chunked(1).map { it.toInt() })
