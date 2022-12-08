import java.io.Reader

data class Coordinate(val x: Int, val y: Int)

@JvmInline
value class Tree(val height: Int)

fun main() {
  fun part1(input: Reader): Int {
    val map = mutableMapOf<Coordinate, Tree>()
    input.useLines { lines ->
      lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
          map[Coordinate(x, y)] = Tree(c.digitToInt())
        }
      }
    }
    return map.count { (coordinate, tree) ->
      map.none { it.key.x == coordinate.x && it.key.y < coordinate.y && it.value.height >= tree.height }
        || map.none { it.key.x == coordinate.x && it.key.y > coordinate.y && it.value.height >= tree.height }
        || map.none { it.key.x < coordinate.x && it.key.y == coordinate.y && it.value.height >= tree.height }
        || map.none { it.key.x > coordinate.x && it.key.y == coordinate.y && it.value.height >= tree.height }
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

  val input = readInput("day08")
  part1(input()).also(::println)
}
