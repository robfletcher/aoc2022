import Tile.Rock
import Tile.Sand
import Tile.Source
import java.io.Reader

enum class Tile {
  Rock, Sand, Source
}

typealias Cave = MutableMap<Coordinate, Tile>

val Cave.xRange
  get() = keys.minOf { it.x }..keys.maxOf { it.x }

val Cave.yRange
  get() = keys.minOf { it.y }..keys.maxOf { it.y }

val Cave.depth
  get() = yRange.last

fun main() {

  fun Cave.draw() {
    StringBuilder().apply {
      yRange.forEach { y ->
        xRange.forEach { x ->
          when (get(Coordinate(x, y))) {
            Rock -> append('#')
            Sand -> append('o')
            Source -> append('+')
            null -> append('.')
          }
        }
        append('\n')
      }
      println(toString())
    }
  }

  fun scanCave(reader: Reader): Cave {
    val cave = mutableMapOf<Coordinate, Tile>()
    reader.forEachLine { line ->
      line
        .split(" -> ")
        .map { it.split(',', limit = 2).let { (x, y) -> Coordinate(x.toInt(), y.toInt()) } }
        .windowed(2)
        .forEach { (prev, next) ->
          val direction = if (prev.x == next.x) Coordinate::y else Coordinate::x
          listOf(direction.get(prev), direction.get(next))
            .sorted()
            .let { (start, end) -> start..end }
            .forEach { i ->
              when (direction) {
                Coordinate::x -> prev.copy(x = i)
                else -> prev.copy(y = i)
              }
                .let { cave[it] = Rock }
            }
        }
    }
    return cave
  }

  fun Cave.findRestPosition(sandPos: Coordinate): Coordinate? =
    if (sandPos.y == depth) {
      null
    } else {
      val nextPos = listOf(sandPos.down(), sandPos.down().left(), sandPos.down().right())
        .firstOrNull { !containsKey(it) }
      if (nextPos == null) {
        sandPos
      } else {
        findRestPosition(nextPos)
      }
    }

  fun part1(reader: Reader): Int {
    val sourcePos = Coordinate(500, 0)
    val cave = scanCave(reader).also { it[sourcePos] = Source }

    var done = false
    while (!done) {
      val rest = cave.findRestPosition(sourcePos.copy(y = 1))
      if (rest == null) {
        done = true
      } else {
        cave[rest] = Sand
      }
    }

    cave.draw()
    return cave.count { it.value == Sand }
  }

  val testInput = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
  """.trimIndent()
  assert(part1(testInput.reader()) == 24)

  val input = readInput("day14")
  part1(input()).also(::println)
}
