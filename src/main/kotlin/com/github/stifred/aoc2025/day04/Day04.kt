package com.github.stifred.aoc2025.day04

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val pd = parseInput(day = 4) { it.asPrintingDepartment() }

  solve(part = 1, benchmark = false) { pd.accessibleByForklift.size }
  solve(part = 2, benchmark = false) { pd.totalAccessibleByForkliftCount }
}

val Grid2D<PaperRoll>.accessibleByForklift get() = findAll(PaperRoll).asSequence().filter { pos ->
  Direction.all.asSequence()
    .map { pos.move(it) }
    .filter { elementAt(it) != null }
    .take(4)
    .count() < 4
}.toSet()

val Grid2D<PaperRoll>.totalAccessibleByForkliftCount: Int get() = sequence {
  val grid = copy()

  while (true) {
    val accessible = grid.accessibleByForklift.takeIf { it.isNotEmpty() } ?: break
    grid.removeAt(accessible)

    yield(accessible.size)
  }
}.sum()

fun String.asPrintingDepartment() = asGrid(mapOf('@' to PaperRoll))

data object PaperRoll
