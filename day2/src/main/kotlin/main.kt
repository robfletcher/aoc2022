import Result.Lose
import Result.Tie
import Result.Win
import java.io.File

enum class Result(val score: Int, val code: String) {
  Lose(0, "X"), Tie(3, "Y"), Win(6, "Z")
}

enum class Shape(val score: Int, val code: String) {
  Rock(1, "A") {
    override val beats by lazy { Scissors }
  },
  Paper(2, "B") {
    override val beats = Rock
  },
  Scissors(3, "C") {
    override val beats = Paper
  };

  abstract val beats: Shape
}

fun String.shape() = Shape.values().single { it.code == this }
fun String.strategy() = Result.values().single { it.code == this }

fun Result.vs(shape: Shape): Shape =
  when (this) {
    Win -> Shape.values().single { it.beats == shape }
    Tie -> shape
    Lose -> shape.beats
  }

fun main() {
  File("input")
    .reader()
    .useLines { lines ->
      lines.fold(0) { score: Int, line: String ->
        val (opponent, intent) = line.split(" ", limit = 2).let { (a, b) -> a.shape() to b.strategy() }
        val move = intent.vs(opponent)
        println("Opponent plays $opponent and we want to $intent so we play $move")
        score + move.score + intent.score
      }
    }
    .also { println("Total score: $it") }
    .also { assert(it == 11258) }
}
