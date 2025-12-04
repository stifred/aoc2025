package com.github.stifred.aoc2025.day02

import com.github.stifred.aoc2025.numbers.asDigits
import com.github.stifred.aoc2025.numbers.toLong
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val ranges = parseInput(day = 2, benchmark = false) { it.asIdRanges() }

  solve(part = 1, benchmark = false) { ranges.invalidIdSum1() }
  solve(part = 2, benchmark = false) { ranges.invalidIdSum2() }
}

fun Collection<IdRange>.invalidIdSum1() = flatMap { it.invalidIds1() }.sumOf { it.toLong() }
fun Collection<IdRange>.invalidIdSum2() = flatMap { it.invalidIds2() }.sumOf { it.toLong() }

data class Id(val text: List<UByte>) {
  fun toLong() = text.toLong()

  fun isInvalid1() = text.size % 2 == 0 && isInvalidOn(text.size / 2)
  fun isInvalid2() = (1..(text.size / 2)).any { isInvalidOn(it) }

  private fun isInvalidOn(check: Int) =
    text.size % check == 0 && text.chunked(check).windowed(2).all { (a, b) -> a == b }
}

class IdRange(from: Long, to: Long) {
  private val ids = (from..to).map { Id(it.asDigits()) }

  fun invalidIds1() = ids.filter { it.isInvalid1() }
  fun invalidIds2() = ids.filter { it.isInvalid2() }
}

fun String.asIdRanges() = nonEmptyLineSequence().first().split(',').map { it.asIdRange() }
private fun String.asIdRange() = split('-').map { it.toLong() }.let { (from, to) -> IdRange(from, to) }
