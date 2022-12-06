import java.io.File

val input = File("inputs/day1")
part1(input).also(::println).also { assert(it == 73211) }
part2(input).also(::println).also { assert(it == 213958) }

fun part1(input: File) =
  input.useLines { lines ->
    lines.fold(0 to 0) { (sum, max), line ->
      if (line.isBlank()) {
        0 to Math.max(max, sum)
      } else {
        sum + line.toInt() to max
      }
    }
  }
    .second

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
