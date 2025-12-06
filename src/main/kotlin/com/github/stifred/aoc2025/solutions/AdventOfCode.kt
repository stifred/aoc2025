package com.github.stifred.aoc2025.solutions

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

private val sessionId by lazy { loadSessionId() }

fun loadPuzzleInput(year: Int = 2025, day: Int): String {
  val fileName = "/tmp/aoc-$year-$day-v1.txt"
  val file = File(fileName);
  if (!file.exists()) {
    println("Downloading input (y=$year d=$day)...\n")
    val host = "https://adventofcode.com/$year/day/$day/input"

    val client = HttpClient.newHttpClient()
    val request = HttpRequest.newBuilder()
      .GET()
      .uri(URI.create(host))
      .header("Cookie", "session=$sessionId;")
      .build()

    val response = client.send(request, HttpResponse.BodyHandlers.ofString())
    if (response.statusCode() != 200) {
      error("Failed to connect to AOC: ${response.statusCode()}")
    }

    file.writeText(response.body(), Charsets.UTF_8)
  }

  return file.readText(Charsets.UTF_8)
}

private fun loadSessionId(): String {
  val fromEnv = System.getenv("AOC_SESSION_ID")
  if (fromEnv != null) {
    return fromEnv
  }

  val file = File(System.getProperty("user.home") + File.separator + ".aoc")
  if (!file.exists()) {
    error("~/.aoc doesn't exist")
  }

  return file.readText(Charsets.UTF_8).trim('\n')
}
