import java.io.Reader

@OptIn(ExperimentalStdlibApi::class)
fun main() {
  operator fun Coordinate.minus(other: Coordinate) =
    Coordinate(x - other.x, y - other.y)

  fun drag(head: Coordinate, next: Coordinate): Coordinate {
    val diff = head - next
    return if ((-1..1).run { diff.x in this && diff.y in this }) {
      next
    } else {
      next.run {
        when {
          diff.x > 0 -> right()
          diff.x < 0 -> left()
          else -> this
        }
      }.run {
        when {
          diff.y > 0 -> down()
          diff.y < 0 -> up()
          else -> this
        }
      }
    }
  }

  fun solve(input: Reader, length: Int): Int {
    val rope = (0..<length).map { Coordinate(0, 0) }.toMutableList()
    val visited = mutableSetOf<Coordinate>()
    input.forEachLine { line ->
      val (direction, distance) = line.split(' ', limit = 2)
      repeat(distance.toInt()) {
        val move = Coordinate.traversals.first { it.name.startsWith(direction, ignoreCase = true) }
        rope[0] = move.invoke(rope[0])
        rope.indices.drop(1).forEach { i ->
          rope[i] = drag(rope[i - 1], rope[i])
        }
        visited.add(rope.last())
      }
    }
    return visited.size
  }

  fun part1(input: Reader) = solve(input, 2)

  fun part2(input: Reader) = solve(input, 10)

  val testInput1 = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
  """.trimIndent()
  assert(part1(testInput1.reader()) == 13)
  assert(part2(testInput1.reader()) == 1)

  val testInput2 = """
    R 5
    U 8
    L 8
    D 3
    R 17
    D 10
    L 25
    U 20
  """.trimIndent()
  assert(part2(testInput2.reader()) == 36)

  val input = readInput("day09")
  part1(input()).also(::println).also { assert(it == 5858) }
  part2(input()).also(::println).also { assert(it == 2602) }
}
