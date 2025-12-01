package com.github.stifred.aoc2025.searching

import kotlin.reflect.KClass

class Search<S : SearchState>(
  private val order: Order<S> = Dijkstra(),
  private val seenSpace: SeenSpace<S> = NoSeenSpace(),
) {
  private data object Skipped : RuntimeException("Skipped")

  private val findlings = ArrayDeque<S>()

  fun reset() {
    order.reset()
    seenSpace.reset()
    findlings.clear()
  }

  fun skip(): Nothing {
    throw Skipped
  }

  fun found(state: S): Nothing {
    findlings.addLast(state)
    throw Skipped
  }

  fun push(state: S) = continueWith(state)

  fun continueWith(state: S): Boolean {
    if (seenSpace.tryMarkSeen(state)) {
      order.push(state)
      return true
    }

    return false
  }


  fun find(func: Search<S>.(state: S) -> Unit): S? {
    while (order.hasNext()) {
      try {
        func(order.next())
      } catch (s: Skipped) {
        // Nothing
      }

      if (findlings.isNotEmpty()) {
        return findlings.removeFirst()
      }
    }

    return null
  }

  fun findAll(func: Search<S>.(state: S) -> Unit): List<S> = buildList {
    while (true) {
      val nextFind = find(func) ?: break
      add(nextFind)
    }
  }
}

fun <S : SearchStateWithKey<K>, K> dijkstra(ignored: KClass<S>) = Search(
  order = Dijkstra(),
  seenSpace = KeyMapSeenSpace<S, K>(),
)

inline fun <reified S : SearchState> bfs() = Search(
  order = BreadthFirst<S>(),
  seenSpace = SetMapSeenSpace<S>(),
)

fun <S : SearchStateWithKey<K>, K> bfs(ignored: KClass<S>) = Search(
  order = BreadthFirst(),
  seenSpace = KeyMapSeenSpace<S, K>(),
)

inline fun <reified S : SearchState> dfs() = Search(
  order = DepthFirst(),
  seenSpace = SetMapSeenSpace<S>(),
)

fun <S : SearchStateWithKey<K>, K> dfs(ignored: KClass<S>) = Search(
  order = DepthFirst(),
  seenSpace = KeyMapSeenSpace<S, K>(),
)
