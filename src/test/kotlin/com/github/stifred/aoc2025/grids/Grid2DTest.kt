package com.github.stifred.aoc2025.grids

import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2.Companion.xy
import com.github.stifred.aoc2025.grids.Direction.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class Grid2DTest {
  @Test
  fun `historians and guards`() {
    val input = """
      ....#.....
      .........#
      ..........
      ..#.......
      .......#..
      ..........
      .#..^.....
      ........#.
      #.........
      ......#...
    """.trimIndent()

    // Parsing
    val map = mapOf(
      '#' to Obstacle,
      '^' to Guard(facing = Up),
      '<' to Guard(facing = Left),
      'v' to Guard(facing = Down),
      '>' to Guard(facing = Right),
    )
    val grid = input.asGrid(map)
    assertEquals(10, grid.width)
    assertEquals(10, grid.height)

    // Initial positions
    assertEquals(Guard(facing = Up), grid.elementAt(4 xy 6))
    assertEquals(4 xy 6, grid.find { it is Guard })
    assertEquals(4 xy 6, grid.find(Guard(facing = Up)))
    assertEquals(setOf(4 xy 0, 9 xy 1, 2 xy 3, 7 xy 4, 1 xy 6, 8 xy 7, 0 xy 8, 6 xy 9), grid.findAll(Obstacle))

    // Copy doesn't ruin original
    val copy = grid.copy()
    copy.move(4 xy 6, 6 xy 6)
    assertEquals(4 xy 6, grid.find { it is Guard })
    assertEquals(6 xy 6, copy.find { it is Guard })

    val visited = sequence {
      var current = grid.find { it is Guard } ?: error("No historian?! WTF")
      var guard = grid.specificElementAt<Guard>(current) ?: error("Where did you go?!")

      while (true) {
        yield(current)
        val ahead = current.move(guard.facing)

        // Escape? End run!
        if (!(ahead).isWithin(grid)) break

        // Obstacle? Turn right!
        if (grid.elementAt(ahead) is Obstacle) {
          guard = guard.withRightTurn()
          grid.put(current, guard, force = true)
          continue
        }

        grid.move(current, ahead)
        current = ahead
      }
    }.distinct().count()
    assertEquals(41, visited)
  }

  sealed class Element
  data class Guard(val facing: Direction) : Element() {
    fun withRightTurn() = Guard(facing = facing.hardRight())
  }
  data object Obstacle : Element()
}
