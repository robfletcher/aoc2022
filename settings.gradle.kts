rootProject.name = "aoc2022"

include(
  *(1..4).map { "day$it" }.toTypedArray()
)
