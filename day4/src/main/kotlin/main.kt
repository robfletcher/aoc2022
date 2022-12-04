import java.io.File

fun part1() {
  File("input")
    .reader()
    .useLines { lines ->
      lines.count { line ->
        line
          .split(',', limit = 2)
          .map(String::parseRange)
          .sortedByDescending { it.size }
          .let { (larger, smaller) ->
            larger.containsAll(smaller)
          }
      }
    }
    .also(::println)
    .also { assert(it == 507) }
}

fun String.parseRange() =
  Regex("""(\d+)-(\d+)""")
    .matchEntire(this)!!
    .groupValues
    .let { (_, a, b) -> a.toInt()..b.toInt() }

fun IntRange.containsAll(other: IntRange) =
  other.first >= first && other.last <= last

val IntRange.size
  get() = (endInclusive - start) + 1

fun main() {
  part1()
}
