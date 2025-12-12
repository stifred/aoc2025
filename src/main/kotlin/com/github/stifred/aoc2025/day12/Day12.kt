package com.github.stifred.aoc2025.day12

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.grids.Vector2.Companion.asVector2
import com.github.stifred.aoc2025.searching.SearchState
import com.github.stifred.aoc2025.searching.dfs
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve
import java.util.Collections.nCopies

fun main() {
  val (shapes, regions) = parseInput(day = 12) { it.asShapesAndRegions() }

  solve(part = 1) { regions.count(Region::canLikelyBeFilled) /* This is some bullshit */ }
  solve(part = 2) { regions.countFillableBy(shapes) }
}

fun List<Region>.countFillableBy(shapes: List<Shape>) = count { it.canBeFilledBy(shapes) }

data class Shape(val original: Set<Vector2>) {
  val size = Vector2(x = original.maxOf { it.x }, y = original.maxOf { it.y })
  val flipped = original.map { Vector2(x = 2 - it.x, y = it.y) }.toSet()
  val rotations = listOf(
    original.map { Vector2(x = 2 - it.y, y = it.x) },
    original.map { Vector2(x = 2 - it.x, y = 2 - it.y) },
    original.map { Vector2(x = it.y, y = 2 - it.x) },
  ).map { it.toSet() }
  val allVariations = setOf(original, flipped) + rotations
}
data class Region(val size: Vector2, val quantities: List<Int>) {
  fun canLikelyBeFilled(): Boolean {
    val totalSize = size.x * size.y
    val neededSize = quantities.sumOf { it * 8 }
    return totalSize >= neededSize
  }

  fun canBeFilledBy(shapes: List<Shape>): Boolean {
    if (!canLikelyBeFilled()) return false

    val needed = quantities.flatMapIndexed { i, c -> nCopies(c, shapes[i]) }

    val search = dfs<FilledRegion>()
    search.continueWith(FilledRegion(grid = Grid2D(bottomRight = size.move(Direction.LeftUp))))

    return search.find { fr ->
      val next = needed.getOrNull(fr.level) ?: found(fr)
      for (variation in next.allVariations) {
        for (x in 0..<(size.x - next.size.x)) {
          for (y in 0..<(size.y - next.size.y)) {
            val positions = variation.map { Vector2(x = it.x + x, y = it.y + y) }
            val grid = fr.grid.copy()
            if (grid.put(positions, true)) {
              continueWith(fr.copy(level = fr.level + 1, grid = grid))
            }
          }
        }
      }
    } != null
  }
}

data class FilledRegion(
  val grid: Grid2D<Boolean>,
  val level: Int = 0,
) : SearchState

fun String.asShapesAndRegions(): Pair<List<Shape>, List<Region>> {
  val chunks = split("\n\n").filter { it.isNotBlank() }

  val shapes = chunks.take(6).mapIndexed { i, c ->
    Shape(original = c.replace("$i:\n", "").asGrid(mapOf('#' to '#')).usedPositions)
  }
  val regions = chunks.last().nonEmptyLineSequence().map { it.split(": ") }.map { (a, b) ->
    Region(size = a.asVector2(), quantities = b.split(' ').map { it.toInt() })
  }.toList()

  return shapes to regions
}
