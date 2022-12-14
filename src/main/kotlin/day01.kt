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
      lines.fold(listOf(0)) { totals, line ->
        if (line.isBlank()) {
          totals + 0
        } else {
          totals.dropLast(1) + (totals.last() + line.toInt())
        }
      }
    }.sortedDescending().take(3).sum()

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

  execute("day01", "Part 1", ::part1)
  execute("day01", "Part 2", ::part2)
}
