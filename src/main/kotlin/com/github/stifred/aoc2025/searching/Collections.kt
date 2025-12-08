package com.github.stifred.aoc2025.searching

inline fun <reified T : Any> Iterable<*>.firstOf(): T = firstOrNull { it is T } as? T ?: error("${T::class} not in iterable")
