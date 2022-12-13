import java.io.Reader

data class Coordinate(val x: Int, val y: Int) {
  fun up() = copy(y = y - 1)
  fun left() = copy(x = x - 1)
  fun down() = copy(y = y + 1)
  fun right() = copy(x = x + 1)

  companion object {
    val traversals = listOf(Coordinate::up, Coordinate::left, Coordinate::down, Coordinate::right)
  }

  override fun toString() = "{$x,$y}"
}
typealias Forest = Map<Coordinate, Int>
typealias Tree = Map.Entry<Coordinate, Int>

fun main() {
  fun mapForest(input: Reader): Forest =
    mutableMapOf<Coordinate, Int>().apply {
      input.useLines { lines ->
        lines.forEachIndexed { y, line ->
          line.forEachIndexed { x, c ->
            put(Coordinate(x, y), c.digitToInt())
          }
        }
      }
    }

  fun Forest.pathToEdge(tree: Tree, traversal: (Coordinate) -> Coordinate) =
    generateSequence(tree.key, traversal).drop(1).takeWhile { get(it) != null }

  fun Forest.isVisibleFromEdge(tree: Tree, traversal: (Coordinate) -> Coordinate) =
    pathToEdge(tree, traversal).none { getValue(it) >= tree.value }

  fun Forest.viewingDistanceFrom(tree: Tree, traversal: (Coordinate) -> Coordinate): Int {
    val path = pathToEdge(tree, traversal)
    val distance = path.indexOfFirst { getValue(it) >= tree.value }
    return if (distance < 0) path.count() else distance + 1
  }

  fun part1(input: Reader): Int =
    mapForest(input).run {
      count { tree ->
        Coordinate.traversals.any { isVisibleFromEdge(tree, it) }
      }
    }

  fun part2(input: Reader): Int =
    mapForest(input).run {
      maxOf { tree ->
        Coordinate.traversals.map { viewingDistanceFrom(tree, it) }.reduce { acc, i -> acc * i }
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
  part1(input()).also(::println).also { assert(it == 1825) }
  part2(input()).also(::println).also { assert(it == 235200) }
}
