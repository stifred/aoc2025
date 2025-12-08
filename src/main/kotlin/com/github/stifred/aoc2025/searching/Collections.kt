package com.github.stifred.aoc2025.searching

inline fun <reified T : Any> Iterable<*>.firstOf(): T = firstOfOrNull<T>() ?: error("${T::class.simpleName} not in iterable")
inline fun <reified T : Any> Iterable<*>.firstOfOrNull(): T? = firstOrNull { it is T } as? T
