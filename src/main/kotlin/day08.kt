import java.io.Reader

data class Coordinate(val x: Int, val y: Int)

fun main() {
  fun mapTrees(input: Reader): Map<Coordinate, Int> {
    val map = mutableMapOf<Coordinate, Int>()
    input.useLines { lines ->
      lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
          map[Coordinate(x, y)] = c.digitToInt()
        }
      }
    }
    return map
  }

  fun part1(input: Reader): Int {
    val map = mapTrees(input)
    return map.count { (coordinate, height) ->
      map.none { it.key.x == coordinate.x && it.key.y < coordinate.y && it.value >= height }
        || map.none { it.key.x == coordinate.x && it.key.y > coordinate.y && it.value >= height }
        || map.none { it.key.x < coordinate.x && it.key.y == coordinate.y && it.value >= height }
        || map.none { it.key.x > coordinate.x && it.key.y == coordinate.y && it.value >= height }
    }
  }

  fun part2(input: Reader): Int {
    val map = mapTrees(input)
    return map.maxOf { (coordinate, height) ->
      val up = map.keys.filter { it.x == coordinate.x && it.y < coordinate.y }.sortedByDescending { it.y }
        .run { indexOfFirst { map.getValue(it) >= height }.let { if (it < 0) size else it + 1 } }
      val left = map.keys.filter { it.x < coordinate.x && it.y == coordinate.y }.sortedByDescending { it.x }
        .run { indexOfFirst { map.getValue(it) >= height }.let { if (it < 0) size else it + 1 } }
      val down = map.keys.filter { it.x == coordinate.x && it.y > coordinate.y }.sortedBy { it.y }
        .run { indexOfFirst { map.getValue(it) >= height }.let { if (it < 0) size else it + 1 } }
      val right = map.keys.filter { it.x > coordinate.x && it.y == coordinate.y }.sortedBy { it.x }
        .run { indexOfFirst { map.getValue(it) >= height }.let { if (it < 0) size else it + 1 } }
      left * right * up * down
    }
  }

  val testInput = """
    30373
    25512
    65332
    33549
    35390
  """.trimIndent()
  assert(part1(testInput.reader()) == 21)
  assert(part2(testInput.reader()) == 8)

  val input = readInput("day08")
  part1(input()).also(::println)
  part2(input()).also(::println)
}
