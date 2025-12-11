package com.github.stifred.aoc2025.day10

import com.github.stifred.aoc2025.searching.SearchState
import com.github.stifred.aoc2025.searching.dfs
import com.github.stifred.aoc2025.solutions.info
import com.github.stifred.aoc2025.solutions.nonEmptyLineSequence
import com.github.stifred.aoc2025.solutions.parseInput
import com.github.stifred.aoc2025.solutions.solve

fun main() {
  val machines = parseInput(day = 10) { it.asLightDiagrams() }
  info("Diagrams") { machines.size }

  solve(part = 1) { machines.totalFewestPressesRequired }
  solve(part = 2) { machines.totalFewestPressesRequiredForJoltages }
}

val List<LightDiagram>.totalFewestPressesRequired get() = sumOf { it.fewestPressesRequired }
val List<LightDiagram>.totalFewestPressesRequiredForJoltages get() = sumOf { it.fewestPressesRequiredForJoltages }

data class Equation(val sum: Int, val parts: Set<Int>) {
  val maximums get() = parts.map { it to sum }
}

fun List<Equation>.maximums() = buildMap {
  for ((k, v) in this@maximums.asSequence().flatMap(Equation::maximums)) {
    merge(k, v) { a, b -> minOf(a, b) }
  }
}

data class LightDiagram(val targetLights: List<Boolean>, val targetJoltages: List<Int>, val buttons: List<Button>) {
  val initial = Indicator(lights = targetLights.map { false })

  val fewestPressesRequired get() = fetchLights().first { it.lights == targetLights }.cost
  val fewestPressesRequiredForJoltages get(): Long {
    val equations = targetJoltages
      .mapIndexed { i, j -> Equation(j, buttons.indices.filter { i in buttons[it] }.toSet()) }
      .sortedBy { it.parts.size }

    return equations.runGaussianElimination()
  }

  fun fetchLights(): Sequence<Indicator> {
    val search = dfs<Indicator>()
    search.continueWith(initial)

    return search.findSequence { machine ->
      if (machine.cost > foundCost) skip()
      if (!found && machine.lights == targetLights) found(machine)
      if (machine.lights == targetLights) found(machine)

      val index = maxOf(0, buttons.indexOf(machine.lastButton))
      for (btn in buttons.subList(index, buttons.size)) continueWith(machine.with(btn))
    }
  }
}

data class Indicator(
  val lights: List<Boolean>,
  val lastButton: Button = emptyList(),
  override val cost: Int = 0,
) : SearchState {
  fun with(button: Button): Indicator {
    val newLights = lights.toMutableList()
    for (i in button) newLights[i] = !lights[i]

    return copy(
      lights = newLights,
      lastButton = button,
      cost = cost + 1,
    )
  }
}

typealias Button = List<Int>

fun String.asLightDiagrams() = nonEmptyLineSequence().map { it.asLightDiagram() }.toList()
private fun String.asLightDiagram(): LightDiagram {
  val words = split(' ').groupBy { it.first() }

  return LightDiagram(
    targetLights = words.getValue('[').first().asTargetState(),
    targetJoltages = words.getValue('{').first().asTargetJoltage(),
    buttons = words.getValue('(').map { it.asButton() },
  )
}
private fun String.asTargetState() = trim('[', ']').map { it == '#' }
private fun String.asTargetJoltage() = trim('{', '}').split(',').map { it.toInt() }
private fun String.asButton(): Button = trim('(', ')').split(',').map { it.toInt() }
