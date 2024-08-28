/**
 * The Environment class represents an execution environment where variables can be defined and accessed.
 * Variables are stored in a map that associates the variable name with a pair containing the declaration
 * and the variable's value.
 */
class Environment {
    // Map that stores the variables, where the key is the variable name and the value is a pair
    // containing the declaration ("const" or "let") and the variable's value.
    private val values: HashMap<String, Pair<String, Any?>> = HashMap()

    /**
     * Defines a new variable in the environment.
     *
     * @param name The name of the variable to be defined.
     * @param value The value associated with the variable.
     * @param declaration Specifies whether the variable is a constant ("const") or a regular variable ("let").
     * @throws IllegalArgumentException If `declaration` is "const" and `value` is `null`.
     */
    fun define(name: String, value: Any?, declaration: String) {
        if (declaration == "const" && value == null) {
            throw IllegalArgumentException("$value cannot be null if declaration is 'const'")
        }
        values[name] = Pair(declaration, value)
    }

    /**
     * Retrieves the value of a variable in the environment.
     *
     * @param name The name of the variable whose value is to be retrieved.
     * @return The value of the variable if it exists.
     * @throws Error If the variable has not been defined in the environment.
     */
    fun get(name: String): Any? {
        if (values.containsKey(name)) {
            return values[name]?.second
        }
        throw Error("Undefined variable '$name'.")
    }

    fun assign(name: String, value: Any?) {

        if (values.containsKey(name)) {
            val valueStored: Pair<String, Any?>? = values[name]
            if (valueStored?.first == "const") {
                throw Exception("Cannot assign variable '$name' to a const.")
            }
            val keyWord  = valueStored?.first ?: throw Error("keyWord for assignation cant be null")
            values[name] = Pair(keyWord, value)
            return
        }

        throw Error("Undefined variable '$name'. and its try to be assigned")

    }


}
