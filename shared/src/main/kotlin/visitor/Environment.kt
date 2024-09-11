package position.visitor

import nodes.Expression
import nodes.StatementType

/**
 * The Environment class represents an execution environment where variables can be defined and accessed.
 * Variables are stored in a map that associates the variable name with a pair containing the declaration
 * and the variable's value.
 */

class Environment(
    val enclosing: Environment? = null, // Previous environment
    initialValues: HashMap<String, StatementType.Variable> = HashMap() // Initial values
) {
    // Map that stores the variables, where the key is the variable name and the value is a pair
    // containing the declaration ("const" or "let") and the variable's value.
    private val values: HashMap<String, StatementType.Variable> = HashMap(initialValues) // Initializes the values map

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
    constructor(initialValues: HashMap<String, StatementType.Variable>, parentEnvironment: Environment? = null) : this(parentEnvironment, initialValues)

    /**
     * Defines a new variable in the environment.
     *
     * @param name The name of the variable to be defined.
     * @param value The value associated with the variable.
     * @param declaration Specifies whether the variable is a constant ("const") or a regular variable ("let").
     * @throws IllegalArgumentException If `declaration` is "const" and `value` is `null`.
     */
    fun define(variable: StatementType.Variable): Environment {
        val newEnvironment = alternativeCopy()

        if (variable.designation == "const" && variable.initializer == null) {
            throw IllegalArgumentException(
                "variable '${variable.identifier}' with modifier 'const' cannot be declared."
            )
        }
        newEnvironment[variable.identifier] = variable
        if (enclosing == null) return Environment(newEnvironment)
        return Environment(enclosing.getCopy(), newEnvironment)
    }

    /**
     * Retrieves the value of a variable in the environment.
     *
     * @param name The name of the variable whose value is to be retrieved.
     * @return The value of the variable if it exists.
     * @throws Error If the variable has not been defined in the environment.
     */
    fun get(name: String): StatementType.Variable {
        val result = values[name]
        if (result == null) {
            throw Error("Undefined variable: $name")
        } else {
            return result
        }
//        if (values.containsKey(name)) {
//            return values[name]?.second
//        }
//        if (enclosing != null) return enclosing.get(name)
//
//        throw Error("Undefined variable '$name'.")
    }

    fun assign(name: String, value: Any?): Environment {
        if (values.containsKey(name)) {
            val newScope = alternativeCopy()
            val valueStored: StatementType.Variable? = newScope[name]

            checkDontReAssignToConst(valueStored, name)

            if (!assignationMatchesDeclaredType(value, valueStored)) {
            }

            val keyWord = valueStored?.designation ?: throw Error("keyWord for assignation cannot be null")
            newScope[name] = StatementType.Variable(
                designation = keyWord,
                identifier = valueStored.identifier,
                initializer = Expression.Literal(value, valueStored.position),
                dataType = valueStored.dataType,
                position = valueStored.position
            )
            return Environment(newScope)
        }
        if (enclosing != null) return Environment(values, enclosing.assign(name, value))

        throw Error("Cannot perform assignation on undefined variable '$name'.")
    }

    private fun assignationMatchesDeclaredType(value: Any?, valueStored: StatementType.Variable?): Boolean {
        return valueStored?.dataType == value
    }

    private fun checkDontReAssignToConst(value: StatementType.Variable?, name: String) {
        if (value?.designation == "const") {
            throw Exception("Cannot assign variable '$name' to a const.")
        }
    }

    /**
     * Creates and returns a deep copy of the current environment.
     *
     * @return A new Environment instance that is a deep copy of the current environment.
     */
//    private fun getDeepCopyFromEnvironment(): HashMap<String, StatementType.Variable> {
//        val deepCopiedValues = HashMap<String, StatementType.Variable>()
//
//        for ((key, value) in values) {
//            val (declaration, originalValue) = value
//
//            // Create a deep copy of the value if necessary
//            val copiedValue = when (originalValue) {
//                is String -> originalValue // Strings are immutable, no need to deep copy
//                is Number -> originalValue // Numbers are immutable, no need to deep copy
//                is List<*> -> ArrayList(originalValue) // Example for a deep copy of a list
//                is Map<*, *> -> HashMap(originalValue as Map<*, *>) // Example for a deep copy of a map
//                else -> originalValue // For other types, just use the original value
//            }
//
//            deepCopiedValues[key] = Pair(declaration, copiedValue)
//        }
//
//        return deepCopiedValues
//    }

    private fun alternativeCopy(): HashMap<String, StatementType.Variable> {
        return HashMap(values)
    }

    fun getCopy(): Environment {
        if (enclosing == null) return Environment(alternativeCopy())
        return Environment(alternativeCopy(), enclosing.getCopy())
    }

    fun contains(varName: String): Boolean {
        return values.containsKey(varName)
    }
}
