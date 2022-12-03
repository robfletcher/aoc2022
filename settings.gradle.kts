rootProject.name = "aoc2022"

include(
  *(1..3).map { "day$it" }.toTypedArray()
)
