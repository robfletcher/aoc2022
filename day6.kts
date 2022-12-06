import java.io.File

val input = File("inputs/day6")
part1(input).also(::println).also { assert(it == 7) }
//part2(input).also(::println).also { assert(it == 7) }

fun part1(input: File): Int {
  val previous = mutableListOf<Char>()
  return input.readText().indexOfFirst { c ->
    previous.add(c)
    while (previous.size > 4) {
      previous.removeFirst()
    }
    previous.distinct().size == 4
  } + 1
}

fun part2(input: File): Int = TODO()
