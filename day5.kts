import java.io.File

val input = File("inputs/day5")
part1(input).also(::println).also { assert(it == "VJSFHWGFT") }
part2(input).also(::println).also { assert(it == "LCTQFBVZV") }

@JvmInline
value class Crate(val label: String)

data class Instruction(val count: Int, val from: Int, val to: Int)

fun part1(input: File) =
  input.solve { stacks, instruction ->
    repeat(instruction.count) {
      stacks[instruction.to - 1].add(stacks[instruction.from - 1].removeLast())
    }
  }

fun part2(input: File) =
  input.solve { stacks, instruction ->
    (0 until instruction.count)
      .map { stacks[instruction.from - 1].removeLast() }
      .reversed()
      .forEach { stacks[instruction.to - 1].add(it) }
  }

fun File.solve(instructionHandler: (List<MutableList<Crate>>, Instruction) -> Unit): String {
  val lines = readLines()
  val stacks = lines.takeWhile { it.isNotBlank() }.parseStacks()
  val instructions = lines.parseInstructions()
  instructions.forEach { instruction ->
    instructionHandler(stacks, instruction)
  }
  return stacks.map { it.lastOrNull() }.filterNotNull().joinToString("") { it.label }
}

fun List<String>.parseStacks(): List<MutableList<Crate>> {
  val stacks = mutableListOf<MutableList<Crate>>()
  reversed().forEach { line ->
    line.chunked(4).forEachIndexed { index, chunk ->
      val parser = Regex("""\[(\w)\]\s?""")
      parser.matchEntire(chunk)?.groupValues?.also { (_, label) ->
        while (index >= stacks.size) {
          stacks.add(mutableListOf<Crate>())
        }
        stacks[index].add(Crate(label))
      }
    }
  }
  return stacks
}

fun List<String>.parseInstructions(): Iterable<Instruction> {
  val parser = Regex("""move (\d+) from (\d+) to (\d+)""")
  return map { instruction ->
    parser
      .matchEntire(instruction)
      ?.groupValues
      ?.drop(1)
      ?.map(String::toInt)
      ?.let { (count, from, to) -> Instruction(count, from, to) }
  }
    .filterNotNull()
}
