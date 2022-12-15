import java.io.Reader
import kotlin.math.abs

fun Coordinate.manhattanDistanceFrom(other: Coordinate) =
  abs(x - other.x) + abs(y - other.y)

data class Sensor(
  val location: Coordinate,
  val closestBeacon: Coordinate
) {
  private val beaconDistance by lazy { location.manhattanDistanceFrom(closestBeacon) }

  fun columnsWithNoBeacons(row: Int) =
    (beaconDistance - abs(location.y - row)).let { x ->
      (location.x - x)..(location.x + x)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun IntRange.exclude(r: IntRange): List<IntRange> {
  if (this.isEmpty() || (r.first <= first && r.last >= last)) return emptyList()
  if (r.isEmpty() || r.first > last || r.last < first) return listOf(this)
  val ranges = mutableListOf<IntRange>()
  if (first < r.first) ranges.add(first..<r.first)
  if (last > r.last) ranges.add(r.last + 1..last)
  return ranges
}

fun excludeColumnsWithNoBeacons(sensors: Iterable<Sensor>, row: Int, columns: IntRange): List<IntRange> =
  sensors.fold(listOf(columns)) { ranges: List<IntRange>, sensor: Sensor ->
    ranges.flatMap { it.exclude(sensor.columnsWithNoBeacons(row)) }
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
    val beaconsExcluded = sensors.flatMapTo(mutableSetOf()) { it.columnsWithNoBeacons(y) }
    val knownBeacons = sensors.map { it.closestBeacon }.filter { it.y == y }.map { it.x }.toSet()
    return (beaconsExcluded - knownBeacons).size
  }

  fun part2(reader: Reader, searchSpace: IntRange): Long {
    val sensors = deploySensors(reader)
    val beaconPosition = searchSpace.firstNotNullOf { y ->
      excludeColumnsWithNoBeacons(sensors, y, searchSpace)
        .firstOrNull()
        ?.let { Coordinate(it.first, y) }
    }
    return (beaconPosition.x * 4000000L) + beaconPosition.y
  }

  assert((1..10).exclude(IntRange.EMPTY) == listOf(1..10))
  assert(IntRange.EMPTY.exclude(1..10).isEmpty())
  assert((1..10).exclude(11..20) == listOf(1..10))
  assert((1..10).exclude(-1..11).isEmpty())
  assert((1..10).exclude(3..8) == listOf(1..2, 9..10))
  assert((1..10).exclude(1..10).isEmpty())
  assert((1..10).exclude(-1..3) == listOf(4..10))
  assert((1..10).exclude(8..13) == listOf(1..7))
  assert((1..10).exclude(2..9) == listOf(1..1, 10..10))

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
  assert(part2(testInput.reader(), 0..20) == 56000011L)

  execute("day15", "Part 1") { input -> part1(input, 2000000) }.also { assert(it == 5878678) }
  execute("day15", "Part 2") { input -> part2(input, 0..4000000) }.also { assert(it == 11796491041245L) }
}
