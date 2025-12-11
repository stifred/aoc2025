package com.github.stifred.aoc2025.searching

data class MapCombinator<K, V>(
  private val map: Map<K, List<V>>,
) : Iterator<List<Pair<K, V>>> {
  private val keyList = map.keys.toList()
  private val indices = (0..<keyList.size).map { 0 }.toMutableList()

  override fun hasNext(): Boolean = indices[0] < map.getValue(keyList.first()).size
  override fun next(): List<Pair<K, V>> {
    if (!hasNext()) error("Does not have next")

    return indices.withIndex()
      .map { (i, ind) -> keyList[i] to map.getValue(keyList[i])[ind] }
      .also { incrementIndexes() }
  }

  private fun incrementIndexes() {
    indices[indices.size - 1] += 1
    for (i in (1..<indices.size).reversed()) {
      if (indices[i] >= map.getValue(keyList[i]).size) {
        indices[i - 1] += 1
        indices[i] = 0
      }
    }
  }

  companion object {
    fun <K, V> Map<K, List<V>>.combinations() = MapCombinator(this)
  }
}
