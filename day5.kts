import java.io.File

val input = File("inputs/day5")
part1(input).also(::println).also { assert(it == "VJSFHWGFT") }
part2(input).also(::println).also { assert(it == "LCTQFBVZV") }

inline class Crate(val label: String)

fun part1(input: File): String {
  val lines = input.readLines()
  val stacks = lines.takeWhile { it.isNotBlank() }.parseStacks()
  stacks.runInstructions(lines) { count, from, to ->
    repeat(count) {
      this[to - 1].add(this[from - 1].removeLast())
    }
  }
  return stacks.map { it.last() }.joinToString("") { it.label }
}

fun part2(input: File): String {
  val lines = input.readLines()
  val stacks = lines.takeWhile { it.isNotBlank() }.parseStacks()
  stacks.runInstructions(lines) { count, from, to ->
    (1..count)
      .map { this[from - 1].removeLast() }
      .reversed()
      .forEach { this[to - 1].add(it) }
  }
  return stacks.map { it.lastOrNull() }.filterNotNull().joinToString("") { it.label }
}

fun List<String>.parseStacks(): List<MutableList<Crate>> {
  val stacks = mutableListOf<MutableList<Crate>>()
  reversed().forEach { line ->
    line.chunked(4).forEachIndexed { index, chunk ->
      val pattern = Regex("""\[(\w)\]\s?""")
      pattern.matchEntire(chunk)?.groupValues?.also { (_, label) ->
        while (index >= stacks.size) {
          stacks.add(mutableListOf<Crate>())
        }
        stacks[index].add(Crate(label))
      }
    }
  }
  return stacks
}

fun List<MutableList<Crate>>.runInstructions(
  lines: Iterable<String>,
  operation: List<MutableList<Crate>>.(Int, Int, Int) -> Unit
) {
  val instructionParser = Regex("""move (\d+) from (\d+) to (\d+)""")
  lines.forEach { instruction ->
    instructionParser.matchEntire(instruction)?.groupValues?.drop(1)?.map(String::toInt)?.also { (count, from, to) ->
      operation(count, from, to)
    }
  }
}
