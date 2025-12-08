package com.github.stifred.aoc2025.day07

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.grids.Vector2WithDirection
import com.github.stifred.aoc2025.grids.Vector2WithDirection.Companion.towards
import com.github.stifred.aoc2025.searching.MemoizationCache
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val manifold = parseInput(day = 7) { it.asTachyonManifold() }

  solve(part = 0) { manifold.sendQuantumBeamAndCountTimelines() }
}

class TachyonManifold(startGrid: Grid2D<Entity>) {
  val start = startGrid.find(Start)!!
  val splitters = startGrid.findAll(Splitter)
}

fun TachyonManifold.sendQuantumBeamAndCountTimelines(
  action: Vector2WithDirection = start towards Direction.Down,
  memoized: MemoizationCache<Vector2, Long> = MemoizationCache(),
): Pair<Int, Long> {
  val last = action.nextPosition
  val next = splitters.asSequence()
    .filter { it.x == last.x }
    .filter { it.y > last.y }
    .minByOrNull { it.y }

  return (if (next != null) {
    memoized.runMemoized(next) {
      Direction.horizontals.sumOf { sendQuantumBeamAndCountTimelines(next towards it, memoized).second }
    }
  } else 1).let { memoized.size to it }
}

fun String.asTachyonManifold() = TachyonManifold(startGrid = asGrid(mapOf('S' to Start, '^' to Splitter)))

sealed class Entity
data object Start : Entity()
data object Splitter : Entity()
