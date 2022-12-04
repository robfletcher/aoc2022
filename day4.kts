import java.io.File

fun part1(input: File) =
  input
    .useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map { it.parseRange() }
          .sortedByDescending { it.size }
          .let { (larger, smaller) -> larger.containsAll(smaller) }
      }
    }

fun String.parseRange() =
  split('-', limit = 2)
    .let { (a, b) -> a.toInt()..b.toInt() }

fun IntRange.containsAll(other: IntRange) =
  other.first >= first && other.last <= last

val IntRange.size
  get() = (endInclusive - start) + 1

fun part2(input: File) =
  input
    .useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map { it.parseRange() }
          .let { (a, b) -> a.overlaps(b) }
      }
    }

fun IntRange.overlaps(other: IntRange) =
  first <= other.last && last >= other.first

val input = File("inputs/day4")
part1(input).also(::println).also { assert(it == 507) }
part2(input).also(::println).also { assert(it == 897) }
