import Result.Lose
import Result.Tie
import Result.Win
import java.io.Reader

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

  fun scoreVs(opponent: Shape) =
    when (opponent) {
      this -> Tie
      beats -> Win
      else -> Lose
    }
}

fun main() {
  fun String.strategy() = Result.values().single { it.code == this }

  fun String.shape() =
    Shape.values().find { it.code == this } ?: Shape.values().single { it.ordinal == strategy().ordinal }

  fun Result.vs(shape: Shape): Shape =
    when (this) {
      Win -> Shape.values().single { it.beats == shape }
      Tie -> shape
      Lose -> shape.beats
    }

  fun part1(input: Reader) =
    input.useLines { lines ->
      lines.fold(0) { score: Int, line: String ->
        val (opponent, move) = line.split(' ', limit = 2).map { it.shape() }
        score + move.score + move.scoreVs(opponent).score
      }
    }

  fun part2(input: Reader) =
    input.useLines { lines ->
      lines.fold(0) { score: Int, line: String ->
        val (opponent, intent) = line.split(' ', limit = 2).let { (a, b) -> a.shape() to b.strategy() }
        val move = intent.vs(opponent)
        score + move.score + intent.score
      }
    }

  val testInput = """
    A Y
    B X
    C Z""".trimIndent()
  assert(part1(testInput.reader()) == 15)
  assert(part2(testInput.reader()) == 12)

  execute("day02", "Part 1", ::part1)
  execute("day02", "Part 2", ::part2)
}
