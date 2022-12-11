import java.io.Reader
import java.util.Scanner
import kotlin.math.floor
import kotlin.streams.toList

data class Monkey(
  val items: MutableList<Long>,
  val operation: (Long) -> Long,
  val test: (Long) -> Int
) {
  private var _inspectCount = 0
  val inspectionCount: Int
    get() = _inspectCount

  fun inspectItem(): Long =
    items.removeFirst().let(operation).also { _inspectCount++ }
}

fun main() {
  fun parseMonkeys(input: Reader): List<Monkey> =
    mutableListOf<Monkey>().apply {
      input.readLines().chunked(7) { it ->
        Monkey(
          items = Scanner(it[1].substringAfter("Starting items: ")).useDelimiter(", ").findAll("""\d+""")
            .map { it.group().toLong() }.toList().toMutableList(),
          operation = """\s*Operation: new = old ([*+]) (old|\d+)""".toRegex()
            .matchEntire(it[2])!!.groupValues.let { (_, op, value) ->
              when (op) {
                "+" -> {
                  { it + if (value == "old") it else value.toLong() }
                }

                "*" -> {
                  { it * if (value == "old") it else value.toLong() }
                }

                else -> error("Unrecognized op: $op")
              }
            },
          test = """\s*Test: divisible by (\d+)\s*If true: throw to monkey (\d+)\s*If false: throw to monkey (\d+)"""
            .toRegex()
            .matchEntire(it.slice(3..5).joinToString(" "))!!
            .groupValues
            .let { (_, divisor, trueIndex, falseIndex) ->
              { if (it % divisor.toLong() == 0L) trueIndex.toInt() else falseIndex.toInt() }
            }
        )
          .also {
            add(it)
          }
      }
    }

  fun runRound(monkeys: List<Monkey>, relief: Double) {
    monkeys.forEach { monkey ->
      while (monkey.items.isNotEmpty()) {
        val worry = floor(monkey.inspectItem() / relief).toLong()
        val index = monkey.test(worry)
        monkeys[index].items.add(worry)
      }
    }
  }

  fun part1(input: Reader) =
    parseMonkeys(input)
      .let { monkeys ->
        repeat(20) {
          runRound(monkeys, 3.0)
        }
        monkeys.map { it.inspectionCount }.sortedDescending().take(2).reduce { acc, it -> acc * it }
      }

  fun part2(input: Reader) =
    parseMonkeys(input)
      .let { monkeys ->
        repeat(10000) {
          runRound(monkeys, 2.75)
        }
        monkeys.map { it.inspectionCount.toLong() }.sortedDescending().also(::println).take(2)
          .reduce { acc, it -> acc * it }
      }

  val testInput = """
    Monkey 0:
      Starting items: 79, 98
      Operation: new = old * 19
      Test: divisible by 23
        If true: throw to monkey 2
        If false: throw to monkey 3
    
    Monkey 1:
      Starting items: 54, 65, 75, 74
      Operation: new = old + 6
      Test: divisible by 19
        If true: throw to monkey 2
        If false: throw to monkey 0
    
    Monkey 2:
      Starting items: 79, 60, 97
      Operation: new = old * old
      Test: divisible by 13
        If true: throw to monkey 1
        If false: throw to monkey 3
    
    Monkey 3:
      Starting items: 74
      Operation: new = old + 3
      Test: divisible by 17
        If true: throw to monkey 0
        If false: throw to monkey 1
  """.trimIndent()
  parseMonkeys(testInput.reader()).also { monkeys ->
    val relief = 3.0

    runRound(monkeys, relief)
    assert(monkeys[0].items == listOf(20L, 23L, 27L, 26L))
    assert(monkeys[1].items == listOf(2080L, 25L, 167L, 207L, 401L, 1046L))
    assert(monkeys[2].items.isEmpty())
    assert(monkeys[3].items.isEmpty())

    runRound(monkeys, relief)
    assert(monkeys[0].items == listOf(695L, 10L, 71L, 135L, 350L))
    assert(monkeys[1].items == listOf(43L, 49L, 58L, 55L, 362L))
    assert(monkeys[2].items.isEmpty())
    assert(monkeys[3].items.isEmpty())
  }

  assert(part1(testInput.reader()) == 10605)

  parseMonkeys(testInput.reader()).also { monkeys ->
    val relief = 1.0

    runRound(monkeys, relief)
    assert(monkeys[0].inspectionCount == 2)
    assert(monkeys[1].inspectionCount == 4)
    assert(monkeys[2].inspectionCount == 3)
    assert(monkeys[3].inspectionCount == 6)

    repeat(19) { runRound(monkeys, relief) }
    assert(monkeys[0].inspectionCount == 99)
    assert(monkeys[1].inspectionCount == 97)
    assert(monkeys[2].inspectionCount == 8)
    assert(monkeys[3].inspectionCount == 103)

    repeat(980) { runRound(monkeys, relief) }
    assert(monkeys[0].inspectionCount == 5204)
    assert(monkeys[1].inspectionCount == 4792)
    assert(monkeys[2].inspectionCount == 199)
    assert(monkeys[3].inspectionCount == 5192)

    repeat(9000) { runRound(monkeys, relief) }
    assert(monkeys[0].inspectionCount == 52166)
    assert(monkeys[1].inspectionCount == 47830)
    assert(monkeys[2].inspectionCount == 1938)
    assert(monkeys[3].inspectionCount == 52013)
  }

  assert(part2(testInput.reader()) == 2713310158L)

  val input = readInput("day11")
  part1(input()).also(::println)
  part2(input()).also(::println)
}
