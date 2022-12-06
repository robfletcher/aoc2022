import java.io.File

object Main

fun readInput(name: String): File =
  File(Main.javaClass.getResource(name)?.file ?: error("input file $name not found"))
