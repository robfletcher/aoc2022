import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import java.io.Reader

fun main() {
  val comparator: Comparator<JsonArray> = object : Comparator<JsonArray> {
    override fun compare(o1: JsonArray, o2: JsonArray): Int {
      val itr1 = o1.iterator()
      val itr2 = o2.iterator()
      while (itr1.hasNext() && itr2.hasNext()) {
        val e1 = itr1.next()
        val e2 = itr2.next()
        if (e1 is JsonPrimitive && e2 is JsonPrimitive) {
          if (e1.int < e2.int) return -1
          else if (e1.int > e2.int) return 1
        } else {
          val delta = compare(
            e1 as? JsonArray ?: JsonArray(listOf(e1)),
            e2 as? JsonArray ?: JsonArray(listOf(e2))
          )
          if (delta != 0) return delta
        }
      }
      return when {
        itr1.hasNext() && !itr2.hasNext() -> 1
        !itr1.hasNext() && itr2.hasNext() -> -1
        else -> 0
      }
    }
  }

  fun String.packet() = Json.parseToJsonElement(this) as JsonArray

  fun part1(reader: Reader): Int {
    val matchingIndices = mutableListOf<Int>()
    reader.useLines { lines ->
      lines.chunked(3).forEachIndexed { index, (a, b) ->
        if (comparator.compare(a.packet(), b.packet()) <= 0) {
          matchingIndices.add(index + 1)
        }
      }
    }
    return matchingIndices.sum()
  }

  fun part2(reader: Reader): Int {
    val m1: JsonArray = "[[2]]".packet()
    val m2: JsonArray = "[[6]]".packet()
    return reader.useLines { lines ->
      lines.mapTo(mutableListOf(m1, m2)) { if (it.isBlank()) null else it.packet() }
    }
      .filterNotNull()
      .sortedWith(comparator)
      .run {
        (indexOf(m1) + 1) * (indexOf(m2) + 1)
      }
  }

  assert(comparator.compare("[1,1,3,1,1]".packet(), "[1,1,5,1,1]".packet()) < 0)
  assert(comparator.compare("[[1],[2,3,4]]".packet(), "[[1],4]".packet()) < 0)
  assert(comparator.compare("[9]".packet(), "[[8,7,6]]".packet()) > 0)
  assert(comparator.compare("[[4,4],4,4]".packet(), "[[4,4],4,4,4]".packet()) < 0)
  assert(comparator.compare("[7,7,7,7]".packet(), "[7,7,7]".packet()) > 0)
  assert(comparator.compare("[]".packet(), "[3]".packet()) < 0)
  assert(comparator.compare("[[[]]]".packet(), "[[]]".packet()) > 0)
  assert(comparator.compare("[1,[2,[3,[4,[5,6,7]]]],8,9]".packet(), "[1,[2,[3,[4,[5,6,0]]]],8,9]".packet()) > 0)

  val testInput = """
    [1,1,3,1,1]
    [1,1,5,1,1]
  
    [[1],[2,3,4]]
    [[1],4]
  
    [9]
    [[8,7,6]]
  
    [[4,4],4,4]
    [[4,4],4,4,4]
  
    [7,7,7,7]
    [7,7,7]
  
    []
    [3]
  
    [[[]]]
    [[]]
  
    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
  """.trimIndent()

  assert(part1(testInput.reader()) == 13)
  assert(part2(testInput.reader()) == 140)

  execute("day13", "Part 1", ::part1)
  execute("day13", "Part 2", ::part2)
}
