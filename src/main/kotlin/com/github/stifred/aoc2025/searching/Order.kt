package com.github.stifred.aoc2025.searching

import java.util.PriorityQueue

sealed interface Order<S : SearchState> : Iterator<S> {
  fun reset()
  fun push(state: S)
}

class BreadthFirst<S : SearchState> : Order<S> {
  private val deque = ArrayDeque<S>()

  override fun reset() = deque.clear()
  override fun push(state: S) = deque.addFirst(state)
  override fun hasNext(): Boolean = deque.isNotEmpty()
  override fun next(): S = deque.removeFirst()
}

class DepthFirst<S : SearchState> : Order<S> {
  private val deque = ArrayDeque<S>()

  override fun reset() = deque.clear()
  override fun push(state: S) = deque.addLast(state)
  override fun hasNext(): Boolean = deque.isNotEmpty()
  override fun next(): S = deque.removeFirst()
}

class Dijkstra<S : SearchState> : Order<S> {
  private val queue = PriorityQueue<S> { a, b -> a.cost - b.cost }

  override fun reset() = queue.clear()
  override fun push(state: S) {
    queue.add(state)
  }
  override fun hasNext(): Boolean = queue.isNotEmpty()
  override fun next(): S = queue.poll()
}

