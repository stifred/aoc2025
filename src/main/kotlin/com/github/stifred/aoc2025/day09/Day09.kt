package com.github.stifred.aoc2025.day09

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.grids.Vector2.Companion.asVector2
import com.github.stifred.aoc2025.grids.Vector2WithDirection
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val theater = parseInput(day = 9) { it.asMovieTheater() }

  solve(part = 1, benchmark = false) { theater.findBiggestRectangles().first().area }
  solve(part = 2, benchmark = false) { theater.findBiggestRedGreenRectangle().area }
}

fun List<Vector2>.findBiggestRectangles() = asSequence()
  .flatMapIndexed { i, l -> asSequence().drop(i + 1).map { l to it } }
  .map { (a, b) -> RectangleWithArea(a, b) }
  .sortedByDescending { it.area }

data class RectangleWithArea(val a: Vector2, val b: Vector2) {
  private val tl = Vector2(x = minOf(a.x, b.x), y = minOf(a.y, b.y))
  private val br = Vector2(x = maxOf(a.x, b.x), y = maxOf(a.y, b.y))

  val area get() = (1 + br.x - tl.x).toLong() * (1 + br.y - tl.y).toLong()

  fun isWithin(path: List<Vector2WithDirection>): Boolean {
    val xInside = (tl.x + 1)..<br.x
    val yInside = (tl.y + 1)..<br.y

    // Eliminate those with red tiles within them immediately:
    if (path.any { it.pos.x in xInside && it.pos.y in yInside }) return false

    // Check if there is a path segment that slices into the rectangle:
    for ((curr, next) in path.asSequence().windowed(2)) {
      val (start, dir) = curr
      val (end) = next

      val overlap = when (dir) {
        Direction.Left -> start.y in yInside && (end.x..start.x).any { it in xInside }
        Direction.Right -> start.y in yInside && (start.x..end.x).any { it in xInside }
        Direction.Up -> start.x in xInside && (end.y..start.y).any { it in yInside }
        Direction.Down -> start.x in xInside && (start.y..end.y).any { it in yInside }
        else -> error("Unexpected: $dir")
      }

      if (overlap) return false
    }

    return true
  }
}

fun List<Vector2>.findBiggestRedGreenRectangle(): RectangleWithArea {
  val redPath = (this + subList(0, 2)).asSequence().windowed(2).map { (a, b) -> a.facing(b) }.toList()

  return findBiggestRectangles().first { it.isWithin(redPath) }
}

fun String.asMovieTheater() = nonEmptyLineSequence().map { it.asVector2() }.toList()
