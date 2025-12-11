package com.github.stifred.aoc2025.day11

import com.github.stifred.aoc2025.searching.MemoizationCache
import com.github.stifred.aoc2025.solutions.info
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val devices = parseInput(day = 11) { it.asDevices() }
  info("Device count") { devices.size }

  solve(part = 1) { devices.countPaths(from = "you", to = "out") }
  solve(part = 2) { devices.countPathsToOutViaDacAndFft(from = "svr") }
}

fun Map<String, Set<String>>.countPaths(
  from: String,
  to: String,
  naughtyList: Set<String> = emptySet(),
  memo: MemoizationCache<String, Long> = MemoizationCache(),
): Long = memo.runMemoized(from) {
  val attachedTo = (get(from) ?: emptySet()).filterNot { it in naughtyList }
  if (to in attachedTo) 1L else attachedTo.sumOf { countPaths(it, to, naughtyList, memo) }
}

fun Map<String, Set<String>>.countPathsToOutViaDacAndFft(from: String): Long {
  val fromDac = countPaths(from, "dac", setOf("fft", "out"))
  val dacFft = countPaths("dac", "fft", setOf(from, "out"))
  val fftOut = countPaths("fft", "out", setOf(from, "dac"))

  val fromFft = countPaths(from, "fft", setOf("dac", "out"))
  val fftDac = countPaths("fft", "dac", setOf(from, "out"))
  val dacOut = countPaths("dac", "out", setOf(from, "fft"))

  return (fromDac * dacFft * fftOut) + (fromFft * fftDac * dacOut)
}

fun String.asDevices() = nonEmptyLineSequence()
  .map { it.split(": ") }
  .map { (b, a) -> b to a.split(' ').toSet() }
  .toMap()
