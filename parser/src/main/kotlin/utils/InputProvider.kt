package utils

import kotlin.collections.get

class InputProvider(private val inputMap: Map<String, String> = emptyMap()) {
  fun input(name: String?): String {
    var input = inputMap[name]
    if (input == null) {
      println(name)
      input = readln()
    }
    return input
  }
}
