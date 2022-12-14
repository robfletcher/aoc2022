import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.io.Reader
import java.util.PriorityQueue
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

typealias Terrain = Map<Coordinate, Char>

data class PathNode(
  val coordinate: Coordinate,
  val distance: Int = Int.MAX_VALUE
) : Comparable<PathNode> {
  override fun compareTo(other: PathNode) = distance.compareTo(other.distance)
}

val Terrain.start
  get() = entries.single { (_, c) -> c == 'S' }.key

val Terrain.goal
  get() = entries.single { (_, c) -> c == 'E' }.key

val Char.height
  get() = when (this) {
    'S' -> 'a'.code
    'E' -> 'z'.code
    else -> code
  }

@OptIn(ExperimentalTime::class)
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

  fun Terrain.pathsFrom(coordinate: Coordinate) =
    coordinate.run {
      setOf(copy(y = y + 1), copy(y = y - 1), copy(x = x + 1), copy(x = x - 1))
    }
      .filter {
        val currentHeight = getValue(coordinate).height
        val adjacentHeight = get(it)?.height
        adjacentHeight != null && adjacentHeight >= currentHeight - 1
      }
      .shuffled()

  /** Dijkstra's algorithm. */
  fun Terrain.findShortestPath(
    from: Coordinate = this.goal,
    to: Coordinate = this.start,
    cache: MutableMap<Coordinate, Int> = mutableMapOf(from to 0)
  ): Int {
    if (cache.containsKey(to)) return cache.getValue(to)
    val queue = (keys - from)
      .map { PathNode(it, cache[it] ?: Int.MAX_VALUE) }
      .let(::PriorityQueue)
    queue.add(PathNode(from, cache.getValue(from)))
    var winner: PathNode? = null
    while (winner == null && queue.isNotEmpty()) {
      val current = queue.remove()
      if (current.coordinate == to) {
        winner = current
      } else if (current.distance != Int.MAX_VALUE) {
        pathsFrom(current.coordinate).forEach { child ->
          val childNode = queue.find { it.coordinate == child }
          if (childNode != null) {
            val distance = current.distance + 1
            if (distance < childNode.distance) {
              queue.remove(childNode)
              queue.add(PathNode(child, distance))
              cache[child] = distance
            }
          }
        }
      }
    }
    return checkNotNull(winner) { "No path found!" }.distance
  }

  fun part1(input: Reader) = runBlocking { parseTerrain(input).findShortestPath() }

  fun part2(input: Reader): Int {
    val terrain = parseTerrain(input)
    val startPoints = terrain.filter { it.value.height == 'a'.height }.keys
    val cache = mutableMapOf(terrain.goal to 0)
    return runBlocking(Default) {
      startPoints.map {
        async {
          terrain.findShortestPath(to = it, cache = cache)
        }
      }.awaitAll().min()
    }
  }

  val testInput = """
    Sabqponm
    abcryxxl
    accszExk
    acctuvwj
    abdefghi
  """.trimIndent()

  assert(part1(testInput.reader()) == 31)
  assert(part2(testInput.reader()) == 29)

  val input = readInput("day12")
  part1(input()).also(::println)
  measureTime {
    part2(input()).also(::println)
  }.also(::println)
}
