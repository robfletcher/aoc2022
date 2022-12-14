import java.io.Reader
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

object Main

fun readInput(name: String): Reader =
  Main.javaClass.getResourceAsStream(name)?.reader() ?: error("input file $name not found")

@OptIn(ExperimentalTime::class)
fun <V> execute(name: String, title: String, block: (Reader) -> V) =
  measureTimedValue { block(readInput(name)) }
    .apply { println("$title\n$value\nin $duration") }
    .value

fun main() {
  try {
    (1..25)
      .map { it.toString().padStart(2, '0') }
      .forEach { n ->
        Class.forName("Day${n}Kt")
          .also { println("Day $n\n------") }
          .getMethod("main")
          .invoke(null)
        println()
      }
  } catch (_: ClassNotFoundException) {
  }
}
