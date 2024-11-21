package position.nodes

enum class Type {
  ANY,
  NUMBER,
  STRING,
  BOOLEAN;
  companion object {
    fun stringToType(type: String): Type {
      return when (type.uppercase()) {
        "ANY" -> ANY
        "NUMBER" -> NUMBER
        "STRING" -> STRING
        "BOOLEAN" -> BOOLEAN
        else -> throw IllegalArgumentException("Unknown type: $type")
      }
    }
  }
}
