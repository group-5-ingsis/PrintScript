package visitor


class Environment {
  private val values: HashMap<String, Any?> = HashMap()
  // This is For Scope of blocks, the other is for all
  fun define(name: String, value: Any?) {
    values[name] = value
  }

  fun get(name: String): Any? {
    if (values.containsKey(name)) {
      return values[name]
    }

    throw Error(
      ("Undefined variable '$name'").toString() + "'."
    )
  }

}
