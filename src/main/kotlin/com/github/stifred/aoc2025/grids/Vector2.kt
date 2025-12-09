package com.github.stifred.aoc2025.grids

import com.github.stifred.aoc2025.grids.Vector2WithDirection.Companion.towards
import com.github.stifred.aoc2025.searching.SearchState
import kotlin.math.abs

data class Vector2(val x: Int, val y: Int) {
  fun move(dir: Direction): Vector2 = this + dir.asVector2

  operator fun minus(other: Vector2) = Vector2(x = x - other.x, y = y - other.y)
  operator fun plus(other: Vector2) = Vector2(x = x + other.x, y = y + other.y)
  operator fun times(int: Int) = Vector2(x = int * x, y = int * y)

  fun isWithin(topLeft: Vector2, bottomRight: Vector2): Boolean {
    return x in (topLeft.x..bottomRight.x) && y in (topLeft.y..bottomRight.y)
  }

  infix fun isWithin(grid: Grid2D<*>) = isWithin(topLeft = grid.topLeft, bottomRight = grid.bottomRight)
  infix fun isOutside(grid: Grid2D<*>) = !isWithin(grid)

  infix fun manhattanTo(other: Vector2) = abs(x - other.x) + abs(y - other.y)

  infix fun touches(other: Vector2) = when {
    x == other.x -> y in (other.y - 1)..(other.y + 1)
    y == other.y -> x in (other.x - 1)..(other.x + 1)
    else -> false
  }

  fun facing(other: Vector2): Vector2WithDirection = when {
    x == other.x -> when {
      y < other.y -> towards(Direction.Down)
      else -> towards(Direction.Up)
    }
    y == other.y -> when {
      x < other.x -> towards(Direction.Right)
      else -> towards(Direction.Left)
    }
    else -> null
  } ?: error("Not in a straight line")

  companion object {
    fun between(a: Vector2, b: Vector2, alt: Boolean = false) = buildSet {
      var current = a
      add(current)
      if (alt) {
        while (current.x != b.x) {
          current = if (current.x < b.x) current.copy(x = current.x + 1) else current.copy(x = current.x - 1)
          add(current)
        }
      }
      while (current.y != b.y) {
        current = if (current.y < b.y) current.copy(y = current.y + 1) else current.copy(y = current.y - 1)
        add(current)
      }
      if (!alt) {
        while (current.x != b.x) {
          current = if (current.x < b.x) current.copy(x = current.x + 1) else current.copy(x = current.x - 1)
          add(current)
        }
      }
    }

    fun Pair<Int, Int>.asVector2() = Vector2(x = first, y = second)

    infix fun Int.xy(other: Int) = (this to other).asVector2()

    fun String.asVector2() = split(',').map { it.toInt() }.let { (x, y) -> Vector2(x, y) }

    val Collection<Vector2>.xPositions get() = asSequence().map { it.x }.toSet()
    val Collection<Vector2>.yPositions get() = asSequence().map { it.y }.toSet()
  }

  override fun toString(): String = "$x,$y"
}

data class Vector2WithDirection(val pos: Vector2, val dir: Direction) : SearchState {
  val nextPosition get() = pos.move(dir)

  companion object {
    infix fun Vector2.towards(dir: Direction) = Vector2WithDirection(this, dir)
    infix fun Direction.at(pos: Vector2) = Vector2WithDirection(pos, this)
  }
}

enum class Direction(val x: Int, val y: Int) {
  Left(-1, 0),
  LeftUp(-1, -1),
  Up(0, -1),
  UpRight(1, -1),
  Right(1, 0),
  RightDown(1, 1),
  Down(0, 1),
  DownLeft(-1, 1),
  ;

  fun flipHorizontal() = when (this) {
    LeftUp -> UpRight
    UpRight -> LeftUp
    RightDown -> DownLeft
    DownLeft -> RightDown
    Left -> Right
    Right -> Left
    else -> this
  }

  fun flipVertical() = when (this) {
    LeftUp -> DownLeft
    DownLeft -> LeftUp
    UpRight -> RightDown
    RightDown -> UpRight
    Up -> Down
    Down -> Up
    else -> this
  }

  fun opposite() = change(4)

  fun softLeft(): Direction = change(-1)
  fun softRight(): Direction = change(1)

  fun hardLeft(): Direction = change(-2)
  fun hardRight(): Direction = change(2)

  fun normals() = when {
    isHorizontal() -> verticals
    isVertical() -> horizontals
    else -> error("")
  }
  fun isHorizontal() = this in horizontals
  fun isVertical() = this in verticals

  val asVector2 get() = Vector2(x, y)

  private fun change(diff: Int): Direction {
    val index = entries.indexOf(this) + diff
    val size = entries.size
    return when {
      index >= size -> entries[index - size]
      index < 0 -> entries[index + size]
      else -> entries[index]
    }
  }

  companion object {
    val all by lazy { entries }
    val horizontals by lazy { setOf(Left, Right) }
    val verticals by lazy { setOf(Up, Down) }
    val nonDiagonals by lazy { setOf(Left, Up, Right, Down) }

    fun Char.asDirectionOrNull() = when (this) {
      '<' -> Left
      '^' -> Up
      '>' -> Right
      'v' -> Down
      else -> null
    }
  }
}
