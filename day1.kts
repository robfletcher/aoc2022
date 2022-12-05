import java.io.File

fun part2(input: File) =
  input.useLines { lines ->
    lines.fold(0 to emptyList<Int>()) { (sum, totals), line ->
      if (line.isBlank()) {
        0 to (totals + sum).sortedDescending().take(3)
      } else {
        sum + line.toInt() to totals
      }
    }
  }
    .let { (_, totals) -> totals.sum() }

val input = File("inputs/day1")
part2(input).also(::println).also { assert(it == 213958) }
