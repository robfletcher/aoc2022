@file:OptIn(ExperimentalStdlibApi::class)

import java.io.File

val values = listOf(' ') + ('a'..'z') + ('A'..'Z')

fun main() {
  part1()
  part2()
}

fun part1() {
  File("input")
    .reader()
    .useLines { lines ->
      lines.fold(0) { total, line ->
        val (a, b) = line.halve()
        total + a.first { it in b }.let(values::indexOf)
      }
    }
    .also(::println)
    .also { assert(it == 7817) }
}

fun String.halve() =
  (length / 2).let { mid ->
    slice(0..<mid) to slice(mid..<length)
  }

fun part2() {
  File("input")
    .reader()
    .useLines { lines ->
      lines.chunked(3).fold(0) { total, (l1, l2, l3) ->
        total + l1.first { it in l2 && it in l3 }.let(values::indexOf)
      }
    }
    .also(::println)
    .also { assert(it == 2444) }
}
