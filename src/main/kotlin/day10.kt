import java.io.Reader
import kotlin.text.StringBuilder

@OptIn(ExperimentalStdlibApi::class)
fun main() {

  fun scan(input: Reader): List<Int> =
    mutableListOf(1).apply {
      val pattern = """addx (-?\d+)""".toRegex()
      input.forEachLine { line ->
        add(last())
        pattern.matchEntire(line)?.groupValues?.also { (_, v) ->
          add(last() + v.toInt())
        }
      }
    }

  fun part1(input: Reader): Int {
    val register = scan(input)
    return register.foldIndexed(0) { i: Int, sum: Int, r: Int ->
      if ((i + 21) % 40 == 0) {
        sum + ((i + 1) * r)
      } else {
        sum
      }
    }
  }

  fun part2(input: Reader): String {
    val register = scan(input)
    return StringBuilder()
      .apply {
        (0..<240).forEach { i ->
          val x = i % 40
          if (i > 0 && x == 0) append('\n')
          if (x in register[i].run { this - 1..this + 1 }) {
            append('#')
          } else {
            append('.')
          }
        }
      }
      .toString()
  }

  val testInput = """
    addx 15
    addx -11
    addx 6
    addx -3
    addx 5
    addx -1
    addx -8
    addx 13
    addx 4
    noop
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx 5
    addx -1
    addx -35
    addx 1
    addx 24
    addx -19
    addx 1
    addx 16
    addx -11
    noop
    noop
    addx 21
    addx -15
    noop
    noop
    addx -3
    addx 9
    addx 1
    addx -3
    addx 8
    addx 1
    addx 5
    noop
    noop
    noop
    noop
    noop
    addx -36
    noop
    addx 1
    addx 7
    noop
    noop
    noop
    addx 2
    addx 6
    noop
    noop
    noop
    noop
    noop
    addx 1
    noop
    noop
    addx 7
    addx 1
    noop
    addx -13
    addx 13
    addx 7
    noop
    addx 1
    addx -33
    noop
    noop
    noop
    addx 2
    noop
    noop
    noop
    addx 8
    noop
    addx -1
    addx 2
    addx 1
    noop
    addx 17
    addx -9
    addx 1
    addx 1
    addx -3
    addx 11
    noop
    noop
    addx 1
    noop
    addx 1
    noop
    noop
    addx -13
    addx -19
    addx 1
    addx 3
    addx 26
    addx -30
    addx 12
    addx -1
    addx 3
    addx 1
    noop
    noop
    noop
    addx -9
    addx 18
    addx 1
    addx 2
    noop
    noop
    addx 9
    noop
    noop
    noop
    addx -1
    addx 2
    addx -37
    addx 1
    addx 3
    noop
    addx 15
    addx -21
    addx 22
    addx -6
    addx 1
    noop
    addx 2
    addx 1
    noop
    addx -10
    noop
    noop
    addx 20
    addx 1
    addx 2
    addx 2
    addx -6
    addx -11
    noop
    noop
    noop
  """.trimIndent()
  assert(part1(testInput.reader()) == 13140)

  val expected = """
    ##..##..##..##..##..##..##..##..##..##..
    ###...###...###...###...###...###...###.
    ####....####....####....####....####....
    #####.....#####.....#####.....#####.....
    ######......######......######......####
    #######.......#######.......#######.....
  """.trimIndent()
  assert(part2(testInput.reader()) == expected)

  val input = readInput("day10")
  part1(input()).also(::println).also { assert(it == 14560) }
  part2(input()).also(::println)
}
