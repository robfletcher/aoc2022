import java.io.File

File("inputs/day1")
  .useLines { lines ->
    lines.fold(0 to emptyList<Int>()) { (sum, totals), line ->
      if (line.isBlank()) {
        0 to (totals + sum).sortedDescending().take(3)
      } else {
        sum + line.toInt() to totals
      }
    }
  }
  .let { (_, totals) -> totals.sum() }
  .also { println("Sum of the largest 3 values: $it") }
  .also { assert(it == 213958) }
