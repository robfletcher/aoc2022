import java.io.Reader
import kotlin.math.max

fun main() {
  fun part1(input: Reader) =
    input.useLines { lines ->
      lines.fold(0 to 0) { (sum, max), line ->
        if (line.isBlank()) {
          0 to max(max, sum)
        } else {
          sum + line.toInt() to max
        }
      }
    }
      .second

  fun part2(input: Reader) =
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

  val testInput = """
    1000
    2000
    3000
    
    4000
    
    5000
    6000
    
    7000
    8000
    9000
    
    10000""".trimIndent()
  assert(part1(testInput.reader()) == 24000)
  assert(part2(testInput.reader()) == 45000)

  val input = readInput("day01")
  part1(input()).also(::println)
  part2(input()).also(::println)
}
