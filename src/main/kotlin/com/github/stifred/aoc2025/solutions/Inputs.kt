package com.github.stifred.aoc2025.solutions

fun String.nonEmptyLineSequence() = lineSequence().filter { it.isNotBlank() }
