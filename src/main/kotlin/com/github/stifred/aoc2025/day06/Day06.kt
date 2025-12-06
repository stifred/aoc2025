package com.github.stifred.aoc2025.day06

import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val ws = parseInput(day = 6) { it.asWorksheet() }

  solve(part = 1, benchmark = false) { ws.kiddieSum }
  solve(part = 2, benchmark = false) { ws.grownupSum }
}

data class Worksheet(val operations: List<Operation>) {
  val kiddieSum get() = operations.sumOf { it.kiddieResult }
  val grownupSum get() = operations.sumOf { it.grownupResult }
}

data class Operation(val kind: Kind, val operands: List<String>) {
  val kiddieResult: Long get() = operands.asSequence()
    .map { it.trim() }
    .map { it.toLong() }
    .result()
  val grownupResult: Long get() = (0..<operands.maxOf { it.length })
    .asSequence()
    .map { i -> operands.map { o -> o[i] } }
    .map { it.joinToString(separator = "") }
    .map { it.trim() }
    .map { it.toLong() }
    .result()

  private fun Sequence<Long>.result(): Long = fold(kind.default, kind::apply)

  sealed class Kind(val default: Long) {
    abstract fun apply(a: Long, b: Long): Long

    object Plus : Kind(default = 0) {
      override fun apply(a: Long, b: Long) = a + b
    }
    object Times : Kind(default = 1) {
      override fun apply(a: Long, b: Long) = a * b
    }
  }
}

fun String.asWorksheet() = buildList {
  val kindMap = mapOf('+' to Operation.Kind.Plus, '*' to Operation.Kind.Times)

  val numberLineCount = nonEmptyLineSequence().count() - 1
  val operators = nonEmptyLineSequence().last().mapIndexedNotNull { i, ch -> if (ch == ' ') null else i to ch }.toMap()
  val boundaries = operators.keys.asSequence().map { it - 2 } + lineSequence().maxOf { it.length - 1 }
  for ((start, operator) in operators) {
    val end = boundaries.first { it > start }
    val operands = nonEmptyLineSequence().take(numberLineCount).map { it.substring(start..end) }.toList()
    add(Operation(kindMap.getValue(operator), operands))
  }
}.let { Worksheet(it) }
