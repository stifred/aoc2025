package com.github.stifred.aoc2025.day01

import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val rotations = parseInput(day = 1) { it.asRotations() }

  solve(part = 1, benchmark = false) { findPassword1(rotations) }
  solve(part = 2, benchmark = false) { findPassword2(rotations) }
}

fun findPassword1(rotations: Collection<Rotation>) = Dial().with(rotations).stopsAtZero
fun findPassword2(rotations: Collection<Rotation>) = Dial().with(rotations).zeroes

data class Dial(val number: Int = 50, val stopsAtZero: Int = 0, val zeroes: Int = 0) {
  fun with(rotations: Collection<Rotation>) = rotations.fold(this) { dial, rotation -> rotation.applyTo(dial) }

  fun withLeft(amount: Int): Dial = when (amount) {
    in 100..Int.MAX_VALUE -> copy(zeroes = zeroes + (amount / 100)).withLeft(amount % 100)
    0 -> if (number == 0) copy(stopsAtZero = stopsAtZero + 1) else this
    else -> when (number) {
      in 2..99 -> copy(number = number - 1)
      1 -> copy(number = 0, zeroes = zeroes + 1)
      else -> copy(number = 99)
    }.withLeft(amount - 1)
  }

  fun withRight(amount: Int): Dial = when (amount) {
    in 100..Int.MAX_VALUE -> copy(zeroes = zeroes + (amount / 100)).withRight(amount % 100)
    0 -> if (number == 0) copy(stopsAtZero = stopsAtZero + 1) else this
    else -> when (number) {
      99 -> copy(number = 0, zeroes = zeroes + 1)
      else -> copy(number = number + 1)
    }.withRight(amount - 1)
  }
}

sealed class Rotation(private val size: Int, private val action: Dial.(Int) -> Dial) {
  fun applyTo(dial: Dial) = dial.action(size)

  class Left(val amount: Int) : Rotation(amount, Dial::withLeft)
  class Right(val amount: Int) : Rotation(amount, Dial::withRight)
}

fun String.asRotations() = nonEmptyLineSequence().map { line ->
  when (line[0]) {
    'L' -> Rotation.Left(amount = line.substring(1).toInt())
    'R' -> Rotation.Right(amount = line.substring(1).toInt())
    else -> error("L or R expected")
  }
}.toList()
