package com.github.stifred.aoc2025.solutions

fun String.nonEmptyLineSequence() = lineSequence().filter { it.isNotBlank() }

fun String.asLongRange() = split('-').map { it.toLong() }.let { (a, b) -> a..b }
