package com.github.stifred.aoc2025.searching

import com.github.stifred.aoc2025.grids.Direction
import com.github.stifred.aoc2025.grids.Grid2D.Companion.asGrid
import com.github.stifred.aoc2025.grids.Vector2
import com.github.stifred.aoc2025.grids.Vector2WithDirection
import com.github.stifred.aoc2025.grids.Vector2WithDirection.Companion.towards
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.concurrent.atomic.AtomicInteger

class SearchTest {
  @Test
  fun `day 17 from last year`() {
    data class Result(val points: Int, val sittingPlaces: Int)

    val inputs = listOf(
      """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
      """.trimIndent() to Result(7036, 45),
      """
        #################
        #...#...#...#..E#
        #.#.#.#.#.#.#.#.#
        #.#.#.#...#...#.#
        #.#.#.#.###.#.#.#
        #...#.#.#.....#.#
        #.#.#.#.#.#####.#
        #.#...#.#.#.....#
        #.#.#####.#.###.#
        #.#.#.......#...#
        #.#.###.#####.###
        #.#.#...#.....#.#
        #.#.#.#####.###.#
        #.#.#.........#.#
        #.#.#.#########.#
        #S#.............#
        #################
      """.trimIndent() to Result(11048, 64),
    )
    for ((input, expected) in inputs) {
      val grid = input.asGrid(mapOf('#' to Wall, 'E' to End, 'S' to Start))
      val start = grid.find(Start)!!
      val end = grid.find(End)!!

      val search = dijkstra(Reindeer::class).apply {
        continueWith(Reindeer(position = start))
      }

      val bestScore = AtomicInteger(Int.MAX_VALUE)
      val finishingReindeer = search.findAll { reindeer ->
        val (position, direction, score, turnPositions) = reindeer

        if (score > bestScore.get()) skip()

        if (position == end) {
          bestScore.set(score)
          found(reindeer.copy(turnPositions = turnPositions + end))
        } else {
          val next = position.move(direction)
          if (grid.elementAt(next) != Wall) {
            continueWith(reindeer.copy(position = next, score = score + 1))
          }

          for (turn in sequenceOf(direction.hardLeft(), direction.hardRight())) {
            val nextAfterTurn = position.move(turn)
            if (grid.elementAt(nextAfterTurn) != Wall) {
              val afterTurn = reindeer.copy(
                position = nextAfterTurn,
                direction = turn,
                turnPositions = turnPositions + position,
                score = score + 1001,
              )

              continueWith(afterTurn)
            }
          }
        }
      }

      val lowestScore = finishingReindeer.first().score
      assertEquals(expected.points, lowestScore)

      val sittingPlaces = finishingReindeer.flatMap { it.touchedFrom(start) }.distinct().count()
      assertEquals(expected.sittingPlaces, sittingPlaces)
    }
  }

  data class Reindeer(
    val position: Vector2,
    val direction: Direction = Direction.Right,
    val score: Int = 0,
    val turnPositions: List<Vector2> = emptyList(),
  ) : SearchStateWithKey<Vector2WithDirection> {
    override val key = position towards direction
    override val cost: Int = score

    fun touchedFrom(start: Vector2) = (sequenceOf(start) + turnPositions).windowed(2).flatMap { (a, b) ->
      Vector2.between(a, b)
    }.toSet()
  }

  sealed class Element
  data object Wall : Element()
  data object Start : Element()
  data object End : Element()
}