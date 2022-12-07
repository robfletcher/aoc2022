import java.io.Reader

sealed interface Node {
  val name: String
  val size: Int
}

data class Directory(
  override val name: String,
  val parent: Directory?,
  val contents: MutableList<Node> = mutableListOf()
) : Node {
  override val size
    get() = contents.sumOf { it.size }

  val root: Directory
    get() = parent?.root ?: this

  val subdirectories: List<Directory>
    get() = listOf(this) + contents.filterIsInstance<Directory>().flatMap { it.subdirectories }
}

data class File(
  override val name: String,
  override val size: Int
) : Node

fun main() {
  fun part1(input: Reader): Int {
    val root = Directory(name = "/", parent = null)
    val itr = input.readLines().listIterator()
    var context = root

    while (itr.hasNext()) {
      val line = itr.next()
      if (line.startsWith("\$ cd")) {
        val target = line.substringAfterLast(' ')
        context = when (target) {
          "/" -> context.root
          ".." -> checkNotNull(context.parent) { "Already at root" }
          else -> context
            .contents
            .filterIsInstance<Directory>()
            .find { it.name == target }
            ?: error("No such directory $target")
        }
      } else if (line == "\$ ls") {
        var done = false
        while (!done && itr.hasNext()) {
          val target = itr.next()
          if (target.startsWith('$')) {
            itr.previous()
            done = true
          } else if (target.startsWith("dir ")) {
            context.contents.add(Directory(name = target.substringAfter(' '), parent = context))
          } else {
            context.contents.add(File(name = target.substringAfter(' '), size = target.substringBefore(' ').toInt()))
          }
        }
      }
    }
    return root.subdirectories.filter { it.size <= 100000 }.sumOf { it.size }
  }

  val testInput = """
    $ cd /
    $ ls
    dir a
    14848514 b.txt
    8504156 c.dat
    dir d
    $ cd a
    $ ls
    dir e
    29116 f
    2557 g
    62596 h.lst
    $ cd e
    $ ls
    584 i
    $ cd ..
    $ cd ..
    $ cd d
    $ ls
    4060174 j
    8033020 d.log
    5626152 d.ext
    7214296 k
  """.trimIndent()
  assert(part1(testInput.reader()) == 95437)

  val input = readInput("day07")
  part1(input()).also(::println)
}
