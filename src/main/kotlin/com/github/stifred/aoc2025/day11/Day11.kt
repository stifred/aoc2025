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
  // First path: requires dac->fft to be possible
  val dacFft = countPaths("dac", "fft", setOf(from, "out"))
  val fromDac = if (dacFft > 0L) countPaths(from, "dac", setOf("fft", "out")) else 0L
  val fftOut = if (fromDac > 0L) countPaths("fft", "out", setOf(from, "dac")) else 0L

  // Second path: requires fft->dac to be possible; ergo only if dac->fft doesn't exist
  val fftDac = if (dacFft == 0L) countPaths("fft", "dac", setOf(from, "out")) else 0L
  val fromFft = if (fftDac > 0L) countPaths(from, "fft", setOf("dac", "out")) else 0L
  val dacOut = if (fromFft > 0L) countPaths("dac", "out", setOf(from, "fft")) else 0L

  return (fromDac * dacFft * fftOut) + (fromFft * fftDac * dacOut)
}

fun String.asDevices() = nonEmptyLineSequence()
  .map { it.split(": ") }
  .map { (b, a) -> b to a.split(' ').toSet() }
  .toMap()
