package com.github.stifred.aoc2025.searching

interface SearchState {
  val cost: Int get() = -1
}

interface SearchStateWithKey<K> : SearchState {
  val key: K
}

