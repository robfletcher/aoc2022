import java.io.File

val values = listOf(' ') + ('a'..'z') + ('A'..'Z')

val input = File("inputs/day3")
part1(input).also(::println).also { assert(it == 7817) }
part2(input).also(::println).also { assert(it == 2444) }

fun part1(input: File) =
  input.useLines { lines ->
    lines.fold(0) { total, line ->
      val (a, b) = line.halve()
      total + a.first { it in b }.let(values::indexOf)
    }
  }

fun String.halve() =
  (length / 2).let { mid ->
    slice(0 until mid) to slice(mid until length)
  }

fun part2(input: File) =
  input.useLines { lines ->
    lines.chunked(3).fold(0) { total, (l1, l2, l3) ->
      total + l1.first { it in l2 && it in l3 }.let(values::indexOf)
    }
  }
