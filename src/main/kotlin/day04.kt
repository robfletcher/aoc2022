import java.io.Reader

fun main() {
  fun String.parseRange() =
    split('-', limit = 2)
      .let { (a, b) -> a.toInt()..b.toInt() }

  fun part1(input: Reader) =
    input.useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map { it.parseRange() }
          .sortedByDescending { it.count() }
          .let { (larger, smaller) -> (smaller - larger).isEmpty() }
      }
    }

  fun part2(input: Reader) =
    input.useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map { it.parseRange() }
          .let { (a, b) -> a.intersect(b).isNotEmpty() }
      }
    }

  val testInput = """
    2-4,6-8
    2-3,4-5
    5-7,7-9
    2-8,3-7
    6-6,4-6
    2-6,4-8
  """.trimIndent()
  assert(part1(testInput.reader()) == 2)
  assert(part2(testInput.reader()) == 4)

  val input = readInput("day04")
  part1(input()).also(::println)
  part2(input()).also(::println)
}
