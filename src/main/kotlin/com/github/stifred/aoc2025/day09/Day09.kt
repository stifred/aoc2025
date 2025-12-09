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

data class RectangleWithArea(val a: Vector2, val b: Vector2) {
  private val xMin = minOf(a.x, b.x)
  private val yMin = minOf(a.y, b.y)
  private val xMax = maxOf(a.x, b.x)
  private val yMax = maxOf(a.y, b.y)

  fun isWithin(path: List<Vector2WithDirection>): Boolean {
    val xRange = (xMin + 1)..<xMax
    val yRange = (yMin + 1)..<yMax

    for ((curr, next) in (path + path[0]).windowed(2)) {
      val (start, dir) = curr
      val (end) = next

      val overlap = when (dir) {
        Direction.Left -> start.y in yRange && (end.x..start.x).any { it in xRange }
        Direction.Right -> start.y in yRange && (start.x..end.x).any { it in xRange }
        Direction.Up -> start.x in xRange && (end.y..start.y).any { it in yRange }
        Direction.Down -> start.x in xRange && (start.y..end.y).any { it in yRange }
        else -> error("Unexpected")
      }

      if (overlap) return false
    }

    return true
  }

  val area get() = (1 + xMax - xMin).toLong() * (1 + yMax - yMin).toLong()
}

fun List<Vector2>.findBiggestRedGreenRectangle(): RectangleWithArea {
  val redPath = (this + this[0]).asSequence().windowed(2).map { (a, b) -> a.facing(b) }.toList()

  return findBiggestRectangles().first { it.isWithin(redPath) }
}

fun String.asMovieTheater() = nonEmptyLineSequence().map { it.asVector2() }.toList()
