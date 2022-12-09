import java.io.Reader

fun main() {
  fun plot(head: Coordinate, tail: Coordinate) {
    (4 downTo 0).forEach { y ->
      (0..5).forEach { x ->
        when (Coordinate(x, y)) {
          head -> print('H')
          tail -> print('T')
          Coordinate(0, 0) -> print('s')
          else -> print('.')
        }
      }
      println()
    }
    println()
  }

  fun part1(input: Reader): Int {
    val visited = mutableSetOf<Coordinate>()
    input.useLines { lines ->
      var head = Coordinate(0, 0)
      var tail = head
      lines.forEach { line ->
        val (direction, distance) = line.split(' ', limit = 2).let { it.first() to it.last().toInt() }
        repeat(distance) {
          val move = Coordinate.traversals.first { it.name.startsWith(direction, ignoreCase = true) }
          head = move.invoke(head)
          val xdiff = head.x - tail.x
          val ydiff = head.y - tail.y
          if (xdiff in (-1..1) && ydiff in (-1..1)) {
            // adjacent
          } else {
            if (xdiff > 0) tail = tail.right()
            else if (xdiff < 0) tail = tail.left()
            if (ydiff > 0) tail = tail.up()
            else if (ydiff < 0) tail = tail.down()
          }
          visited.add(tail)
        }
      }
    }
    return visited.size
  }

  val testInput = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
  """.trimIndent()
  assert(part1(testInput.reader()) == 13)

  val input = readInput("day09")
  part1(input()).also(::println)
}
