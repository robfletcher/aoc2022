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
        val element1 = itr1.next()
        val element2 = itr2.next()
        if (element1 is JsonPrimitive && element2 is JsonPrimitive) {
          if (element1.int < element2.int) return -1
          else if (element1.int > element2.int) return 1
        } else if (element1 is JsonArray && element2 is JsonArray) {
          val comparison = compare(element1, element2)
          if (comparison != 0) return comparison
        } else if (element1 is JsonArray && element2 is JsonPrimitive) {
          val comparison = compare(element1, JsonArray(listOf(element2)))
          if (comparison != 0) return comparison
        } else if (element1 is JsonPrimitive && element2 is JsonArray) {
          val comparison = compare(JsonArray(listOf(element1)), element2)
          if (comparison != 0) return comparison
        }
      }

      return if (!itr1.hasNext()) -1 else if (!itr2.hasNext()) 1 else 0
    }
  }

  fun parse(packet: String) = Json.parseToJsonElement(packet) as JsonArray

  fun part1(reader: Reader): Int {
    val matchingIndices = mutableListOf<Int>()
    reader.useLines { lines ->
      lines.chunked(3).forEachIndexed { index, (a, b) ->
        if (comparator.compare(parse(a), parse(b)) <= 0) {
          matchingIndices.add(index + 1)
        }
      }
    }
    return matchingIndices.also(::println).sum()
  }

  assert(comparator.compare(parse("[1,1,3,1,1]"), parse("[1,1,5,1,1]")) < 0)
  assert(comparator.compare(parse("[[1],[2,3,4]]"), parse("[[1],4]")) < 0)
  assert(comparator.compare(parse("[9]"), parse("[[8,7,6]]")) > 0)
  assert(comparator.compare(parse("[[4,4],4,4]"), parse("[[4,4],4,4,4]")) < 0)
  assert(comparator.compare(parse("[7,7,7,7]"), parse("[7,7,7]")) > 0)
  assert(comparator.compare(parse("[]"), parse("[3]")) < 0)
  assert(comparator.compare(parse("[[[]]]"), parse("[[]]")) > 0)
  assert(comparator.compare(parse("[1,[2,[3,[4,[5,6,7]]]],8,9]"), parse("[1,[2,[3,[4,[5,6,0]]]],8,9]")) > 0)

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

  val input = readInput("day13")
  part1(input()).also(::println).also { assert(it < 5532) }
}
