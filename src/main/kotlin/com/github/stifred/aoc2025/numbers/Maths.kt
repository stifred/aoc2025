package com.github.stifred.aoc2025.numbers

fun Sequence<Long>.product() = fold(1L, Long::times)
fun Iterable<Long>.product() = fold(1L, Long::times)
