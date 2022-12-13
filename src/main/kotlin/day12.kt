import java.io.Reader

typealias Terrain = Map<Coordinate, Char>
typealias Path = List<Coordinate>

val Terrain.start
  get() = entries.single { (_, c) -> c == 'S' }.key

val Terrain.goal
  get() = entries.single { (_, c) -> c == 'E' }.key

val Char.height
  get() = when(this) {
    'S' -> 'a'.code
    'E' -> 'z'.code
    else -> code
  }

fun main() {
  fun parseTerrain(input: Reader): Terrain {
    val terrain = mutableMapOf<Coordinate, Char>()
    input.useLines { lines ->
      lines.forEachIndexed { y, line ->
        line.toCharArray().forEachIndexed { x, c ->
          terrain[Coordinate(x, y)] = c
        }
      }
    }
    return terrain
  }

  fun Terrain.pathsFrom(coordinate: Coordinate) = coordinate.run {
    setOf(copy(y = y + 1), copy(y = y - 1), copy(x = x + 1), copy(x = x - 1))
  }
    .filter {
      val currentHeight = getValue(coordinate).height
      val adjacentHeight = get(it)?.height
      adjacentHeight != null && adjacentHeight <= currentHeight + 1
    }
    .toSet()

  fun Terrain.findPathsToEndFrom(path: Path): Set<Path> {
    val nextSteps = pathsFrom(path.last())
      .filter { it !in path }
    val end = nextSteps.find { getValue(it) == 'E' }
    return if (end == null) {
      nextSteps
        .also {
          if (it.isNotEmpty()) {
            println("Searching ${it.size} paths from ${path.last()} with length ${path.size}")
          }
        }
        .flatMap { findPathsToEndFrom(path + it) }
        .also {
          if (it.isNotEmpty()) {
            println("Found ${it.size} paths from ${path.last()}")
          } else {
            println("Dead end at ${path.last()}")
          }
        }
        .toSet()
    } else {
      setOf(path + end)
        .also { println("Found successful path with length ${it.first().size}") }
    }
  }

  fun part1(input: Reader): Int {
    val terrain = parseTerrain(input)
    val initial: Path = listOf(terrain.start)

    return terrain.findPathsToEndFrom(initial)
      .also { println("Found ${it.size} paths with lengths ${it.map(List<*>::size)}") }
      .minOf { it.size -1 }
  }

  val testInput = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
  """.trimIndent()

  assert(part1(testInput.reader()) == 31)

  val input = readInput("day12")
  part1(input()).also(::println)
}
