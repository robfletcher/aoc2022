import java.io.Reader

fun main() {
  fun String.indexOfFirstUniqueSequence(requiredLength: Int) =
    indices.first { i ->
      slice(i until i + requiredLength).toSet().size == requiredLength
    } + requiredLength

  fun part1(input: Reader) = input.readText().indexOfFirstUniqueSequence(4)

  fun part2(input: Reader) = input.readText().indexOfFirstUniqueSequence(14)

  val testInputs = mapOf(
    "mjqjpqmgbljsphdztnvjfqwrcgsmlb" to 7..19,
    "bvwbjplbgvbhsrlpgdmjqwftvncz" to 5..23,
    "nppdvjthqldpwncqszvftbrmjlhg" to 6..23,
    "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg" to 10..29,
    "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw" to 11..26
  )
  testInputs.forEach {(input, results) ->
    assert(part1(input.reader()) == results.first)
    assert(part2(input.reader()) == results.last)
  }

  execute("day06", "Part 1", ::part1)
  execute("day06", "Part 2", ::part2)
}
