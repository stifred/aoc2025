package com.github.stifred.aoc2025.searching

class MemoizationCache<K : Any, V : Any> {
  private val map = mutableMapOf<K, V>()

  val size get() = map.size

  fun clear() = map.clear()
  fun runMemoized(key: K, func: (K) -> V): V = map[key] ?: func(key).also { map[key] = it }
}
