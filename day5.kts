import java.io.File
import java.util.Stack

inline class Crate(val label: String)

fun part1(input: File): String {
  return input.useLines {
    val lines = it.toList()

    val stacks = parseStacks(lines.takeWhile { it.isNotBlank() }.toList().reversed())

    val instructionParser = Regex("""move (\d+) from (\d+) to (\d+)""")
    lines.forEach { instruction ->
      instructionParser.matchEntire(instruction)?.groupValues?.drop(1)?.map(String::toInt)?.also { (count, from, to) ->
        repeat(count) {
          stacks[to - 1].push(stacks[from - 1].pop())
        }
      }
    }
    stacks.map { it.peek() }.joinToString("") { it.label }
  }
}

fun parseStacks(input: List<String>): List<Stack<Crate>> {
  val stacks = mutableListOf<Stack<Crate>>()
  input.forEach { line ->
    line.chunked(4).forEachIndexed { index, chunk ->
      val pattern = Regex("""\[(\w)\]\s?""")
      pattern.matchEntire(chunk)?.groupValues?.also { (_, label) ->
        while (index >= stacks.size) {
          stacks.add(Stack<Crate>())
        }
        stacks[index].push(Crate(label))
      }
    }
  }
  return stacks
}

val input = File("inputs/day5")
part1(input).also(::println).also { assert(it == "VJSFHWGFT") }
//part2(input).also(::println).also { assert(it == 897) }
