import java.io.Reader

@JvmInline
value class Crate(val label: String)

data class Instruction(val count: Int, val from: Int, val to: Int)

fun main() {
  fun List<String>.parseStacks(): List<MutableList<Crate>> {
    val stacks = mutableListOf<MutableList<Crate>>()
    reversed().forEach { line ->
      line.chunked(4).forEachIndexed { index, chunk ->
        val parser = Regex("""\[(\w)]\s?""")
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

  fun Reader.solve(instructionHandler: (List<MutableList<Crate>>, Instruction) -> Unit): String {
    val lines = readLines()
    val stacks = lines.takeWhile { it.isNotBlank() }.parseStacks()
    val instructions = lines.parseInstructions()
    instructions.forEach { instruction ->
      instructionHandler(stacks, instruction)
    }
    return stacks.map { it.lastOrNull() }.filterNotNull().joinToString("") { it.label }
  }

  fun part1(input: Reader) =
    input.solve { stacks, instruction ->
      repeat(instruction.count) {
        stacks[instruction.to - 1].add(stacks[instruction.from - 1].removeLast())
      }
    }

  fun part2(input: Reader) =
    input.solve { stacks, instruction ->
      (0 until instruction.count)
        .map { stacks[instruction.from - 1].removeLast() }
        .reversed()
        .forEach { stacks[instruction.to - 1].add(it) }
    }

  val testInput = """
        [D]    
    [N] [C]    
    [Z] [M] [P]
     1   2   3 
    
    move 1 from 2 to 1
    move 3 from 1 to 3
    move 2 from 2 to 1
    move 1 from 1 to 2
  """.trimIndent()
  assert(part1(testInput.reader()) == "CMZ")
  assert(part2(testInput.reader()) == "MCD")

  execute("day05", "Part 1", ::part1)
  execute("day05", "Part 2", ::part2)
}
