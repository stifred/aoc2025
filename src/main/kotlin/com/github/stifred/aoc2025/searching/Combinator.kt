package com.github.stifred.aoc2025.searching

data class Combinator<T>(
  private val candidates: List<T>,
  private val combinationSize: Int,
  private val unique: Boolean = false,
) : Iterator<List<T>> {
  private val indices = (0..<combinationSize).map { 0 }.toMutableList()

  override fun hasNext(): Boolean = indices.all { it < candidates.size }
  override fun next(): List<T> {
    if (!hasNext()) error("Empty")

    return indices.map { candidates[it] }.also { incrementIndexes() }
  }

  private fun incrementIndexes() {
    if (unique) {
      val minIndex = indices.first()
      val maxIndex = indices.last()
      if (minIndex == maxIndex) {
        indices[indices.size - 1] += 1
      } else {
        for (i in indices.indices.reversed()) {
          if (indices[i] == minIndex) {
            indices[i] += 1
            return
          }
        }
      }
    } else {
      indices[indices.size - 1] += 1
      for (i in (1..<indices.size).reversed()) {
        if (indices[i] >= candidates.size) {
          indices[i - 1] += 1
          indices[i] = 0
        }
      }
    }
  }
}

fun <T> List<T>.combinator(size: Int, unique: Boolean = false) = Combinator(this, size, unique)
