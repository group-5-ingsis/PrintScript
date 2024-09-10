/**
 * The Environment class represents an execution environment where variables can be defined and accessed.
 * Variables are stored in a map that associates the variable name with a pair containing the declaration
 * and the variable's value.
 */

typealias VisitorResultExpressions = Pair<Any?, Environment>
class Environment(
    val enclosing: Environment? = null, // Previous environment
    initialValues: HashMap<String, Pair<String, Any?>> = HashMap() // Initial values
) {
    // Map that stores the variables, where the key is the variable name and the value is a pair
    // containing the declaration ("const" or "let") and the variable's value.
    private val values: HashMap<String, Pair<String, Any?>> = HashMap(initialValues) // Initializes the values map

    /**
     * Primary constructor: Initializes the environment with an empty HashMap and no parent environment.
     */
    constructor() : this(null, HashMap())

    /**
     * Secondary constructor: Initializes the environment with an existing HashMap and an optional parent environment.
     *
     * @param initialValues The initial values for the environment.
     * @param parentEnvironment The parent environment to inherit from.
     */
    constructor(initialValues: HashMap<String, Pair<String, Any?>>, parentEnvironment: Environment? = null) : this(parentEnvironment, initialValues)


    /**
     * Defines a new variable in the environment.
     *
     * @param name The name of the variable to be defined.
     * @param value The value associated with the variable.
     * @param declaration Specifies whether the variable is a constant ("const") or a regular variable ("let").
     * @throws IllegalArgumentException If `declaration` is "const" and `value` is `null`.
     */
    fun define(name: String, value: Any?, declaration: String): Environment {
        val newEnvironment = getDeepCopyFromEnvironment()

        if (declaration == "const" && value == null) {
            throw IllegalArgumentException("$value cannot be null if declaration is 'const'")
        }
        newEnvironment[name] = Pair(declaration, value)

        return Environment(newEnvironment)
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
        if (enclosing != null) return enclosing.get(name);

        throw Error("Undefined variable '$name'.")
    }

    fun assign(name: String, value: Any?): Environment {
        if (values.containsKey(name)) {
            val newScope = getDeepCopyFromEnvironment()
            val valueStored: Pair<String, Any?>? = newScope[name]

            checkDontReAssignToConst(valueStored, name)

            val keyWord = valueStored?.first ?: throw Error("keyWord for assignation cant be null")
            newScope[name] = Pair(keyWord, value)
            return Environment(newScope)
        }
        if (enclosing != null) return Environment(values, enclosing.assign(name, value))

        throw Error("Undefined variable '$name'. and its try to be assigned")
    }

    private fun checkDontReAssignToConst(value: Pair<String, Any?>?, name: String) {
        if (value?.first == "const") {
            throw Exception("Cannot assign variable '$name' to a const.")
        }
    }

    /**
     * Creates and returns a deep copy of the current environment.
     *
     * @return A new Environment instance that is a deep copy of the current environment.
     */
    private fun getDeepCopyFromEnvironment(): HashMap<String, Pair<String, Any?>> {
        val deepCopiedValues = HashMap<String, Pair<String, Any?>>()

        for ((key, value) in values) {
            val (declaration, originalValue) = value

            // Create a deep copy of the value if necessary
            val copiedValue = when (originalValue) {
                is String -> originalValue // Strings are immutable, no need to deep copy
                is Number -> originalValue // Numbers are immutable, no need to deep copy
                is List<*> -> ArrayList(originalValue) // Example for a deep copy of a list
                is Map<*, *> -> HashMap(originalValue as Map<*, *>) // Example for a deep copy of a map
                else -> originalValue // For other types, just use the original value
            }

            deepCopiedValues[key] = Pair(declaration, copiedValue)
        }

        return deepCopiedValues
    }

    fun getCopy(): Environment {
        if (enclosing == null) return Environment(getDeepCopyFromEnvironment())
        return Environment(getDeepCopyFromEnvironment(), enclosing.getCopy())
    }

    fun contains(varName: String): Boolean {
        return values.containsKey(varName)
    }
}
