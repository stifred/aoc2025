package com.github.stifred.aoc2025.day11

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class DeviceTest {
  @Test
  fun `device test input`() {
    val deviceList1 = """
      aaa: you hhh
      you: bbb ccc
      bbb: ddd eee
      ccc: ddd eee fff
      ddd: ggg
      eee: out
      fff: out
      ggg: out
      hhh: ccc fff iii
      iii: out
    """.trimIndent().asDevices()
    assertEquals(5, deviceList1.countPaths(from = "you", to = "out"))

    val deviceList2 = """
      svr: aaa bbb
      aaa: fft
      fft: ccc
      bbb: tty
      tty: ccc
      ccc: ddd eee
      ddd: hub
      hub: fff
      eee: dac
      dac: fff
      fff: ggg hhh
      ggg: out
      hhh: out
    """.trimIndent().asDevices()
    assertEquals(2, deviceList2.countPathsToOutViaDacAndFft(from = "svr"))
  }
}
