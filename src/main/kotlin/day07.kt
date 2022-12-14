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

  val directoryTree: List<Directory>
    get() = listOf(this) + contents.filterIsInstance<Directory>().flatMap { it.directoryTree }
}

data class File(
  override val name: String,
  override val size: Int
) : Node

fun main() {
  fun buildFilesystem(input: Reader): Directory {
    val root = Directory(name = "/", parent = null)
    var context = root
    input.forEachLine { line ->
      when {
        line.startsWith("\$ cd") ->
          context = when (val target = line.substringAfterLast(' ')) {
            "/" -> context.root
            ".." -> checkNotNull(context.parent) { "Already at root" }
            else -> context
              .contents
              .filterIsInstance<Directory>()
              .find { it.name == target }
              ?: error("No such directory $target")
          }

        line.startsWith("dir ") ->
          Directory(name = line.substringAfter(' '), parent = context)
            .also { context.contents.add(it) }

        line.contains("""^\d+""".toRegex()) ->
          line.split(' ', limit = 2)
            .let { (size, name) -> File(name = name, size = size.toInt()) }
            .also { context.contents.add(it) }
      }
    }
    return root
  }

  fun part1(input: Reader) = buildFilesystem(input)
    .directoryTree
    .filter { it.size <= 100000 }
    .sumOf { it.size }

  fun part2(input: Reader): Int {
    val root = buildFilesystem(input)
    val totalSpace = 70000000
    val freeSpace = totalSpace - root.size
    val needToFree = 30000000 - freeSpace
    return root.directoryTree.filter { it.size >= needToFree }.minOf { it.size }
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
  assert(part2(testInput.reader()) == 24933642)

  execute("day07", "Part 1", ::part1)
  execute("day07", "Part 2", ::part2)
}
