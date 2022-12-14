import Tile.Rock
import Tile.Sand
import Tile.Source
import java.io.Reader
import kotlin.math.max
import kotlin.math.min

enum class Tile {
  Rock, Sand, Source
}

typealias Cave = MutableMap<Coordinate, Tile>

val Cave.xRange
  get() = keys.minOf { it.x }..keys.maxOf { it.x }

val Cave.yRange
  get() = keys.minOf { it.y }..keys.maxOf { it.y }

val Cave.depth
  get() = keys.maxOf { it.y }

fun main() {
  fun Cave.draw() {
    StringBuilder().apply {
      yRange.forEach { y ->
        xRange.forEach { x ->
          when (get(Coordinate(x, y))) {
            Rock -> append('#')
            Sand -> append('o')
            Source -> append('+')
            null -> append(' ')
          }
        }
        append('\n')
      }
      println(toString())
    }
  }

  val sourcePos = Coordinate(500, 0)

  fun Pair<Coordinate, Coordinate>.forEachBetween(action: (Coordinate) -> Unit) {
    for (x in min(first.x, second.x)..max(first.x, second.x)) {
      for (y in min(first.y, second.y).. max(first.y, second.y)) {
        action(Coordinate(x, y))
      }
    }
  }

  fun scanCave(reader: Reader) =
    mutableMapOf(sourcePos to Source).apply {
      reader.forEachLine { line ->
        line
          .split(" -> ")
          .map { it.split(',', limit = 2).let { (x, y) -> Coordinate(x.toInt(), y.toInt()) } }
          .windowed(2)
          .forEach { (start, end) ->
            (start to end).forEachBetween { put(it, Rock) }
          }
      }
    }

  fun Cave.findRest(current: Coordinate, depth: Int): Coordinate? =
    listOf(current.down(), current.down().left(), current.down().right())
      .firstOrNull { !containsKey(it) }
      .let { nextPos ->
        if (nextPos == null) {
          current
        } else if (nextPos.y == depth) {
          null
        } else {
          findRest(nextPos, depth)
        }
      }

  fun Cave.findRestWithFloor(current: Coordinate, floor: Int): Coordinate =
    listOf(current.down(), current.down().left(), current.down().right())
      .firstOrNull { !containsKey(it) }
      .let { nextPos ->
        if (nextPos == null || nextPos.y == floor) {
          current
        } else {
          findRestWithFloor(nextPos, floor)
        }
      }

  fun part1(input: Reader): Int {
    val cave = scanCave(input)

    var done = false
    while (!done) {
      val rest = cave.findRest(sourcePos, cave.depth)
      if (rest == null) {
        done = true
      } else {
        cave[rest] = Sand
      }
    }
    return cave.apply { draw() }.count { it.value == Sand }
  }

  fun part2(input: Reader): Int {
    val cave = scanCave(input)
    val floor = cave.depth + 2

    var done = false
    while (!done) {
      val rest = cave.findRestWithFloor(sourcePos, floor)
      cave[rest] = Sand
      if (rest == sourcePos) {
        done = true
      }
    }
    return cave.apply { draw() }.count { it.value == Sand }
  }

  val testInput = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
  """.trimIndent()
  assert(part1(testInput.reader()) == 24)
  assert(part2(testInput.reader()) == 93)

  execute("day14", "Part 1", ::part1)
  execute("day14", "Part 2", ::part2)
}
