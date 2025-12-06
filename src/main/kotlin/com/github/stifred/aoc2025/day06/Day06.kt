package com.github.stifred.aoc2025.day06

import com.github.stifred.aoc2025.numbers.product
import com.github.stifred.aoc2025.solutions.loadPuzzleInput
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val sheet = loadPuzzleInput(day = 6)

  solve(part = 1, benchmark = false) { sheet.asCephalopodProblems().sum }
  solve(part = 2, benchmark = false) { sheet.asCephalopodProblemsWithGrownupsInTheRoom().sum }
}

fun String.asCephalopodProblems(): Sequence<Problem> {
  val numbers = nonEmptyLineSequence()
    .takeWhile { l -> l.any(Char::isDigit) }
    .flatMap { it.split(' ') }
    .filter { it.isNotBlank() }
    .map { it.toLong() }
    .toList()

  return nonEmptyLineSequence().last()
    .splitToSequence(' ')
    .filter { it.isNotBlank() }
    .mapIndexed { i, op ->
      Problem(
        operator = op[0].asOperator(),
        operands = (i..numbers.size).step(1000).mapNotNull(numbers::getOrNull),
      )
    }
}


fun String.asCephalopodProblemsWithGrownupsInTheRoom() = sequence {
  val lines = nonEmptyLineSequence().toList()
  val operands = mutableListOf<Long>()
  for (x in (0..<lines[0].length).reversed()) {
    var number = 0L
    var operator: Problem.Operator? = null
    for (y in lines.indices) {
      when (val char = lines[y][x]) {
        in '0'..'9' -> { number = (number * 10L) + (char.code - '0'.code) }
        '*', '+' -> {
          operator = char.asOperator()
          break
        }
      }
    }

    if (number > 0) operands += number

    if (operator != null) {
      yield(Problem(operator, operands.toList()))
      operands.clear()
    }
  }
}

val Sequence<Problem>.sum get() = sumOf(Problem::result)
private fun Char.asOperator() = Problem.Operator.entries.first { it.char == this }

data class Problem(val operator: Operator, val operands: List<Long>) {
  val result get() = when (operator) {
    Operator.Addition -> operands.sum()
    Operator.Multiplication -> operands.product()
  }

  enum class Operator(val char: Char) { Addition('+'), Multiplication('*') }
}
