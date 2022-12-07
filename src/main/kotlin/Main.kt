import java.io.Reader

object Main

fun readInput(name: String): () -> Reader = {
  Main.javaClass.getResourceAsStream(name)?.reader() ?: error("input file $name not found")
}
