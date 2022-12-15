import java.io.Reader
import kotlin.math.abs

fun Coordinate.manhattanDistanceFrom(other: Coordinate) =
  abs(x - other.x) + abs(y - other.y)

data class Sensor(
  val location: Coordinate,
  val closestBeacon: Coordinate
) {
  val beaconDistance by lazy { location.manhattanDistanceFrom(closestBeacon) }

  private val exclusionZone: MutableMap<Int, IntRange> = mutableMapOf()
  fun columnsWithNoBeacons(row: Int): IntRange =
    exclusionZone.computeIfAbsent(row) { y ->
      (beaconDistance - abs(location.y - y)).let { x ->
        (location.x - x)..(location.x + x)
      }
    }
}

fun Iterable<Sensor>.columnsWithNoBeacons(row: Int): Set<Int> =
  flatMapTo(mutableSetOf()) { s ->
    s.columnsWithNoBeacons(row)
  }

fun main() {
  fun deploySensors(reader: Reader) =
    reader.useLines { lines ->
      lines.mapNotNull { line ->
        """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)"""
          .toRegex()
          .matchEntire(line)
          ?.groupValues
          ?.drop(1)
          ?.map { it.toInt() }
          ?.let { (sx, sy, bx, by) ->
            Sensor(
              location = Coordinate(sx, sy),
              closestBeacon = Coordinate(bx, by)
            )
          }
      }
        .toList()
    }

  fun part1(reader: Reader, y: Int): Int {
    val sensors = deploySensors(reader)
    return (sensors
      .flatMapTo(mutableSetOf()) { it.columnsWithNoBeacons(y) }
      - sensors.flatMap { if (it.closestBeacon.y == y) setOf(it.closestBeacon.x) else emptySet() }.toSet()).size
  }

  fun part2(reader: Reader, searchSpace: IntRange): Int {
    val sensors = deploySensors(reader)
    val beaconPosition = searchSpace.firstNotNullOf { y ->
      println("Checking $y")
      val x = (searchSpace - sensors.columnsWithNoBeacons(y)).firstOrNull()
      x?.let { Coordinate(it, y) }
    }
    println("Found beacon at $beaconPosition")
    return (beaconPosition.x * 4000000) + beaconPosition.y
  }

  val testInput = """
    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    Sensor at x=9, y=16: closest beacon is at x=10, y=16
    Sensor at x=13, y=2: closest beacon is at x=15, y=3
    Sensor at x=12, y=14: closest beacon is at x=10, y=16
    Sensor at x=10, y=20: closest beacon is at x=10, y=16
    Sensor at x=14, y=17: closest beacon is at x=10, y=16
    Sensor at x=8, y=7: closest beacon is at x=2, y=10
    Sensor at x=2, y=0: closest beacon is at x=2, y=10
    Sensor at x=0, y=11: closest beacon is at x=2, y=10
    Sensor at x=20, y=14: closest beacon is at x=25, y=17
    Sensor at x=17, y=20: closest beacon is at x=21, y=22
    Sensor at x=16, y=7: closest beacon is at x=15, y=3
    Sensor at x=14, y=3: closest beacon is at x=15, y=3
    Sensor at x=20, y=1: closest beacon is at x=15, y=3
  """.trimIndent()

  assert(part1(testInput.reader(), 10) == 26)
  assert(part2(testInput.reader(), 0..20) == 56000011)

  execute("day15", "Part 1") { input -> part1(input, 2000000) }.let { assert(it == 5878678) }
  execute("day15", "Part 2") { input -> part2(input, 0..4000000) } //.let { assert(it == 5878678) }
}
