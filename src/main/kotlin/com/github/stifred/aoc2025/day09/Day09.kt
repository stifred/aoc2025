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

  solve(part = 1) { theater.findBiggestRectangles().first().area }
  solve(part = 2) { theater.findBiggestRedGreenRectangle().area }
}

fun List<Vector2>.findBiggestRectangles() = asSequence()
  .flatMapIndexed { i, l -> asSequence().drop(i + 1).map { l to it } }
  .map { (a, b) -> RectangleWithArea(a, b) }
  .sortedByDescending { it.area }

fun List<Vector2>.findBiggestRedGreenRectangle(): RectangleWithArea {
  val redPath = (this + subList(0, 2)).asSequence().windowed(2).map { (a, b) -> a.facing(b) }.toList()

  return findBiggestRectangles().first { it.isWithin(redPath) }
}

class RectangleWithArea(a: Vector2, b: Vector2) {
  private val tl = Vector2(x = minOf(a.x, b.x), y = minOf(a.y, b.y))
  private val br = Vector2(x = maxOf(a.x, b.x), y = maxOf(a.y, b.y))

  val area = (1 + br.x - tl.x).toLong() * (1 + br.y - tl.y).toLong()

  fun isWithin(path: List<Vector2WithDirection>): Boolean {
    val xInside = (tl.x + 1)..<br.x // These are the ranges within the...
    val yInside = (tl.y + 1)..<br.y // ...rectangles; no tiles should be here

    // Eliminate those with red tiles inside them immediately:
    if (path.any { it.pos.x in xInside && it.pos.y in yInside }) return false

    // Check if there is a outline segment that slices INTO the rectangle:
    return path.asSequence().windowed(2).none { (curr, next) ->
      val (start, dir) = curr
      val (end) = next

      when (dir) {
        Direction.Left -> start.y in yInside && end.x < xInside.first && start.x > xInside.last
        Direction.Right -> start.y in yInside && start.x < xInside.last && end.x > xInside.first
        Direction.Up -> start.x in xInside && end.y < yInside.first && start.y > yInside.last
        Direction.Down -> start.x in xInside && start.y < yInside.last && end.y > yInside.first
        else -> error("Unexpected: $dir")
      }
    }
  }
}

fun String.asMovieTheater() = nonEmptyLineSequence().map { it.asVector2() }.toList()
