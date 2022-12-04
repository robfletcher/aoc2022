import java.io.File

fun part1() {
  File("input")
    .reader()
    .useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map(String::parseRange)
          .sortedByDescending(IntRange::size)
          .let { (larger, smaller) -> larger.containsAll(smaller) }
      }
    }
    .also(::println)
    .also { assert(it == 507) }
}

fun String.parseRange() =
  split('-', limit = 2)
    .let { (a, b) -> a.toInt()..b.toInt() }

fun IntRange.containsAll(other: IntRange) =
  other.first >= first && other.last <= last

val IntRange.size
  get() = (endInclusive - start) + 1

fun part2() {
  File("input")
    .reader()
    .useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map(String::parseRange)
          .let { (a, b) -> a.overlaps(b) }
      }
    }
    .also(::println)
    .also { assert(it == 897) }
}

fun IntRange.overlaps(other: IntRange) =
  first <= other.last && last >= other.first

fun main() {
  part1()
  part2()
}
