package com.github.stifred.aoc2025.numbers

fun Sequence<Long>.product() = fold(1L, Long::times)
fun Iterable<Long>.product() = fold(1L, Long::times)

fun Sequence<Int>.product() = fold(1, Int::times)
fun Iterable<Int>.product() = fold(1, Int::times)
