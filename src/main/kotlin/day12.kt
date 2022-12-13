import java.io.Reader
import java.util.PriorityQueue

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
        adjacentHeight != null && adjacentHeight <= currentHeight + 1
      }
      .shuffled()

  /** Dijkstra's algorithm. */
  fun Terrain.findShortestPath(): Int {
    val queue = keys.minus(start).map(::PathNode).let(::PriorityQueue)
    queue.add(PathNode(start, 0))
    var winner: PathNode? = null
    while (winner == null && queue.isNotEmpty()) {
      val current = queue.remove()
      println("Evaluating ${current.coordinate}")
      if (current.coordinate == goal) {
        winner = current
      } else {
        pathsFrom(current.coordinate).forEach { child ->
          val childNode = queue.find { it.coordinate == child }
          if (childNode != null) {
            val distance = current.distance + 1
            if (distance < childNode.distance) {
              queue.remove(childNode)
              queue.add(PathNode(child, distance))
            }
          }
        }
      }
    }
    return checkNotNull(winner) { "No path found!" }.distance
  }

  fun part1(input: Reader) = parseTerrain(input).findShortestPath()

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