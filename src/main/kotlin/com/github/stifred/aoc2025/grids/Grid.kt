package com.github.stifred.aoc2025.grids

import com.github.stifred.aoc2025.grids.Vector2.Companion.asVector2
import com.github.stifred.aoc2025.grids.Vector2.Companion.xy

class Grid2D<E : Any>(
  val topLeft: Vector2 = 0 xy 0,
  val bottomRight: Vector2 = Int.MAX_VALUE xy Int.MAX_VALUE,
  private val elements: MutableMap<Vector2, E> = mutableMapOf(),
) {
  val width = 1 + bottomRight.x - topLeft.x
  val height = 1 + bottomRight.y - topLeft.y

  fun put(pos: Vector2, element: E, force: Boolean = false): Boolean {
    if (!force && elements[pos] != null) return false

    elements[pos] = element
    return true
  }

  fun move(from: Vector2, to: Vector2, force: Boolean = false): Boolean {
    if (!(to isWithin this)) return false
    val source = elementAt(from) ?: return false
    val target = elementAt(to)
    if (target != null && !force) return false

    elements[to] = source
    elements.remove(from)
    return true
  }

  fun elementAt(pos: Vector2) = elements[pos]
  inline fun <reified T : E> specificElementAt(pos: Vector2) = elementAt(pos) as? T

  fun find(e: E): Vector2? = find { it == e }
  fun find(predicate: (E) -> Boolean) = findAll(predicate).firstOrNull()
  fun findAll(e: E): Set<Vector2> = findAll { it == e }
  fun findAll(predicate: (E) -> Boolean): Set<Vector2> = elements.filterValues(predicate).keys

  fun removeAt(pos: Vector2) = elements.remove(pos)
  fun removeAt(pos: Collection<Vector2>) = pos.forEach { removeAt(it) }

  val usedPositions: Set<Vector2> get() = elements.keys

  val freePositions get() = sequence {
    for (x in topLeft.x..bottomRight.x) {
      for (y in topLeft.y..bottomRight.y) {
        val pos = x xy y
        if (elementAt(pos) == null) yield(pos)
      }
    }
  }

  fun copy() = Grid2D(topLeft = topLeft, bottomRight = bottomRight, elements.toMutableMap())

  companion object {
    fun <E : Any> String.asGrid(map: Map<Char, E>) = asGrid { map[it] }

    fun <E : Any> String.asGrid(mapper: (Char) -> E?): Grid2D<E> {
      val lines = lines()
      val height = lines.size
      val width = lines.firstOrNull()?.length ?: 0
      val grid = Grid2D<E>(bottomRight = (width - 1) xy (height - 1))

      for ((y, line) in lines.withIndex()) {
        for ((x, char) in line.withIndex()) {
          val element = mapper(char) ?: continue

          grid.put((x to y).asVector2(), element)
        }
      }

      return grid.copy()
    }
  }
}
