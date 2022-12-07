import java.io.Reader

object Main

fun readInput(name: String): () -> Reader = {
  Main.javaClass.getResourceAsStream(name)?.reader() ?: error("input file $name not found")
}

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
