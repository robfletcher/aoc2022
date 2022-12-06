import java.io.File

fun main() {
  fun String.indexOfFirstUniqueSequence(requiredLength: Int) =
    indices.first { i ->
      slice(i until i + requiredLength).toSet().size == requiredLength
    } + requiredLength

  fun part1(input: File) = input.readText().indexOfFirstUniqueSequence(4)

  fun part2(input: File) = input.readText().indexOfFirstUniqueSequence(14)

  val input = readInput("day06")
  part1(input).also(::println).also { assert(it == 1757) }
  part2(input).also(::println).also { assert(it == 2950) }
}
