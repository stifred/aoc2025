package com.github.stifred.aoc2025.numbers

fun Int.asDigits() = sequence {
  if (this@asDigits < 0) error("Negative value")

  var remaining = this@asDigits
  while (remaining > 0) {
    yield((remaining % 10).toUByte())
    remaining /= 10
  }
}.toList().reversed()

fun Long.asDigits() = sequence {
  if (this@asDigits < 0) error("Negative value")

  var remaining = this@asDigits
  while (remaining > 0) {
    yield((remaining % 10).toUByte())
    remaining /= 10
  }
}.toList().reversed()

fun List<UByte>.toInt() = fold(0) { acc, ub -> (acc * 10) + ub.toInt() }
fun List<UByte>.toLong() = fold(0L) { acc, ub -> (acc * 10) + ub.toLong() }
