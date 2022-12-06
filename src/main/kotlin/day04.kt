import java.io.File

fun main() {
  fun String.parseRange() =
    split('-', limit = 2)
      .let { (a, b) -> a.toInt()..b.toInt() }

  fun part1(input: File) =
    input.useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map { it.parseRange() }
          .sortedByDescending { it.count() }
          .let { (larger, smaller) -> (smaller - larger).isEmpty() }
      }
    }

  fun part2(input: File) =
    input.useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map { it.parseRange() }
          .let { (a, b) -> a.intersect(b).isNotEmpty() }
      }
    }

  val input = readInput("day04")
  part1(input).also(::println).also { assert(it == 507) }
  part2(input).also(::println).also { assert(it == 897) }
}
