import java.io.Reader

fun main() {
  val values = listOf(' ') + ('a'..'z') + ('A'..'Z')

  fun String.halve() =
    (length / 2).let { mid ->
      slice(0 until mid) to slice(mid until length)
    }

  fun part1(input: Reader) =
    input.useLines { lines ->
      lines.fold(0) { total, line ->
        val (a, b) = line.halve()
        total + a.first { it in b }.let(values::indexOf)
      }
    }

  fun part2(input: Reader) =
    input.useLines { lines ->
      lines.chunked(3).fold(0) { total, (l1, l2, l3) ->
        total + l1.first { it in l2 && it in l3 }.let(values::indexOf)
      }
    }

  val testInput = """
    vJrwpWtwJgWrhcsFMMfFFhFp
    jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
    PmmdzqPrVvPwwTWBwg
    wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
    ttgJtRGJQctTZtZT
    CrZsJsPPZsGzwwsLwLmpwMDw
  """.trimIndent()
  assert(part1(testInput.reader()) == 157)
  assert(part2(testInput.reader()) == 70)

  val input = readInput("day03")
  part1(input()).also(::println)
  part2(input()).also(::println)
}
