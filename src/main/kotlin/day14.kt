import Tile.Rock
import Tile.Sand
import Tile.Source
import java.io.Reader
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

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

@OptIn(ExperimentalTime::class)
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

  val sourcePos = Coordinate(500, 0)

  fun scanCave(reader: Reader) =
    mutableMapOf(sourcePos to Source).apply {
      reader.forEachLine { line ->
        line
          .split(" -> ")
          .map { it.split(',', limit = 2).let { (x, y) -> Coordinate(x.toInt(), y.toInt()) } }
          .windowed(2)
          .forEach { (start, end) ->
            val direction = if (start.x == end.x) Coordinate::y else Coordinate::x
            listOf(direction.get(start), direction.get(end))
              .let { it.min()..it.max() }
              .forEach { i ->
                put(if (direction == Coordinate::x) start.copy(x = i) else start.copy(y = i), Rock)
              }
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
    return cave.count { it.value == Sand }
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
    return cave.count { it.value == Sand }
  }

  val testInput = """
    498,4 -> 498,6 -> 496,6
    503,4 -> 502,4 -> 502,9 -> 494,9
  """.trimIndent()
  assert(part1(testInput.reader()) == 24)
  assert(part2(testInput.reader()) == 93)

  val input = readInput("day14")

  measureTimedValue { part1(input()) }.also(::println)
  measureTimedValue { part2(input()) }.also(::println)
}
