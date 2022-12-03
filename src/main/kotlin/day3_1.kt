import java.io.File

val values = (('a'..'z') + ('A'..'Z'))
  .joinToString(separator = "", prefix = "=")

fun main() {
  File("inputs/day3")
    .reader()
    .useLines { lines ->
      lines.fold(0) { total: Int, line: String ->
        val (a, b) = line.halve()
        total + a.first { it in b }.let(values::indexOf)
      }
        .also(::println)
        .also { assert(it == 7817) }
    }
}

fun String.halve(): Pair<String, String> =
  (length / 2).let { mid ->
    slice(0 until mid) to slice(mid until length)
  }
    .also { (a, b) -> check(a.length == b.length) }
