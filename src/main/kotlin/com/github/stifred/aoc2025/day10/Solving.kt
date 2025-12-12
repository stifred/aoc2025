package com.github.stifred.aoc2025.day10

import com.github.stifred.aoc2025.gauss.Matrix
import com.github.stifred.aoc2025.numbers.size
import kotlin.collections.mapNotNull
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt
import kotlin.math.roundToLong

fun List<Equation>.solveWithGaussianElimination(): Long {
  // Make matrix and transform it
  val maximums = maximums()
  val buttons = (0..maxOf { it.parts.max() })
  val matrix = Matrix(width = 2 + buttons.last, height = size)
  for ((r, equation) in withIndex()) {
    for (c in equation.parts) matrix.putValue(r, c, 1)
    matrix.putValue(r, matrix.width - 1, equation.sum)
  }
  matrix.runGaussJordanElimination()

  // Build equations from matrix
  val equationsFromMatrix = (0..<matrix.height).map { r ->
    buildList {
      for (c in 0..<(matrix.width - 1)) {
        val multiplier = matrix.valueAt(r, c)
        if (multiplier != 0.0) add(FixedEquationPart(btn = c, multiplier = multiplier))
      }
      add(FixedEquationPart(multiplier = -matrix.valueAt(r, matrix.width - 1)))
    }.let { FixedEquation(it) }
  }

  // Simplify equations and find optional buttons
  val eqButtons = buttons.associateWith { equationsFromMatrix.equationForOrNull(it) }.filter { it.value != null }
  val freeButtons = buttons.filter { it !in eqButtons }

  // For each combo of free buttons (or once if there are no free buttons),
  return freeButtons.combos(maximums).mapNotNull { combo ->
    val valueSet = combo.toMutableMap()

    while (valueSet.size < buttons.size) {
      for ((btn, eq) in eqButtons.filter { it.key !in valueSet }) {
        val value = eq!!.valueWith(valueSet)
        if (value != null) {
          // '[...]; there's no such thing as "0.5 presses" (nor can you push a button a negative number of times).'
          if (!value.isNatural || value.isNegative) return@mapNotNull null

          valueSet[btn] = value
          break
        }
      }
    }

    valueSet.asSequence().sumOf { it.value }.roundToLong()
  }.min()
}

private val Double.isNatural get() = abs(this - round(this)) < 0.01
private val Double.isNegative get() = roundToInt() < 0

private fun List<Int>.combos(maximums: Map<Int, Int>) = sequence {
  val currents: MutableList<Int> = indices.map { 0 }.toMutableList()
  val maximums = associateWith { maximums.getValue(it) }

  if (isEmpty()) {
    yield(mapOf())
    return@sequence
  }

  while (currents[0] <= maximums.getValue(first())) {
    yield(mapIndexed { i, btn -> btn to currents[i].toDouble() }.toMap())

    currents[size - 1] += 1
    for (i in (1..<size).reversed()) {
      if (currents[i] >= maximums.getValue(get(i))) {
        currents[i - 1] += 1
        currents[i] = 0
      }
    }
  }
}

private fun List<FixedEquation>.equationForOrNull(btn: Int) =
  mapNotNull { it.equationForOrNull(btn) }.takeIf { it.size == 1 }?.first()

data class FixedEquation(val parts: List<FixedEquationPart>) {
  fun equationForOrNull(btn: Int): FixedEquation? {
    val btnPart = parts.firstOrNull { it.btn == btn } ?: return null

    return parts.asSequence()
      .filter { it.btn != btn }
      .map { it.copy(multiplier = it.multiplier / -btnPart.multiplier) }
      .toList()
      .let { FixedEquation(it) }
  }

  fun valueWith(raws: Map<Int, Double>) = parts.map { it.valueWith(raws) }
    .takeIf { l -> l.all { it != null } }?.sumOf { it!! }
}

data class FixedEquationPart(val btn: Int? = null, val multiplier: Double = 1.0) {
  fun valueWith(raws: Map<Int, Double>) = if (btn == null) multiplier else raws[btn]?.let { it * multiplier }
}
