package com.github.stifred.aoc2025.gauss

import kotlin.math.abs
import kotlin.math.round

data class Matrix(
  val width: Int,
  val height: Int,
  private val initialValue: Double = 0.0,
  private val data: MutableMap<Pair<Int, Int>, Double> = mutableMapOf(),
) {
  fun runGaussJordanElimination() {
    var searchRow = 0
    var searchCol = 0

    // Thanks for the pseudocode, Google AI (– and Wikipedia as well)

    while (searchRow < height && searchCol < width) {
      val rowWithMax = (searchRow..<height).maxBy { i -> abs(valueAt(i, searchCol)) }
      if (!valueAt(rowWithMax, searchCol).isZero) {
        if (searchRow != rowWithMax) swapRows(searchRow, rowWithMax)

        // Normalize pivot row
        val factor = valueAt(searchRow, searchCol)
        for (j in searchCol..<width) {
          putValue(searchRow, j, valueAt(searchRow, j) / factor)
        }

        // Eliminate other entries in pivot column
        for (i in 0..<height) {
          if (i == searchRow) continue

          val f = valueAt(i, searchCol)
          for (j in searchCol..<width) {
            putValue(i, j, valueAt(i, j) - (valueAt(searchRow, j) * f))
          }
        }

        // Move to next row
        searchRow += 1
      }

      // Move to next column
      searchCol += 1
    }
  }

  private fun swapRows(ra: Int, rb: Int) {
    val aRow = dataRow(ra)
    val bRow = dataRow(rb)

    for (c in 0..<width) {
      putValue(ra, c, bRow[c])
      putValue(rb, c, aRow[c])
    }
  }

  fun valueAt(r: Int, c: Int) = data[r to c] ?: initialValue
  fun putValue(r: Int, c: Int, value: Double) { data[r to c] = value }
  fun putValue(r: Int, c: Int, value: Int) = putValue(r, c, value.toDouble())

  fun dataRow(r: Int) = (0..<width).map { valueAt(r, it) }

  companion object {
    private val Double.isZero get() = abs(this) < 0.001
  }
}
