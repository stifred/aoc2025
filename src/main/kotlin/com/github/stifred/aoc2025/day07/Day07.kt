package com.github.stifred.aoc2025.day07

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.grids.Vector2WithDirection
import com.github.stifred.aoc2025.grids.Vector2WithDirection.Companion.towards
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val manifold = parseInput(day = 7) { it.asTachyonManifold() }

  solve(part = 1) { manifold.sendBeamAndFindSplits().size }
  solve(part = 2) { manifold.sendQuantumBeamAndCountTimelines() }
}

class TachyonManifold(startGrid: Grid2D<Entity>) {
  val start = startGrid.find(Start)!!
  val splitters = startGrid.findAll(Splitter)
}

fun TachyonManifold.sendBeamAndFindSplits(
  position: Vector2 = start,
  taken: MutableSet<Vector2> = mutableSetOf(),
): Set<Vector2> {
  val next = splitters.asSequence()
    .filter { it.x == position.x }
    .filter { it.y > position.y }
    .minByOrNull { it.y }

  return when (next) {
    in taken -> setOf()
    null -> setOf()
    else -> {
      Direction.horizontals
        .map { next.move(it) }
        .flatMap { sendBeamAndFindSplits(it, taken) }
        .fold(setOf(next)) { set, res -> set + res }
        .also { taken += next }
    }
  }
}

fun TachyonManifold.sendQuantumBeamAndCountTimelines(
  action: Vector2WithDirection = start towards Direction.Down,
  memoized: MutableMap<Vector2, Long> = mutableMapOf(),
): Long {
  val last = action.nextPosition
  val next = splitters.asSequence()
    .filter { it.x == last.x }
    .filter { it.y > last.y }
    .minByOrNull { it.y }

  return if (next != null) {
    if (memoized[next] != null) {
      return memoized.getValue(next)
    }

    Direction.horizontals
      .sumOf { sendQuantumBeamAndCountTimelines(next towards it, memoized) }
      .also { memoized[next] = it }
  } else 1
}

fun String.asTachyonManifold() = TachyonManifold(startGrid = asGrid(mapOf('S' to Start, '^' to Splitter)))

sealed class Entity
data object Start : Entity()
data object Splitter : Entity()
