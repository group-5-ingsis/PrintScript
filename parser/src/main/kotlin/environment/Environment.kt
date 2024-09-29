package environment

import nodes.Expression
import nodes.Statement

class Environment(

    val enclosing: Environment? = null,
    private val values: List<Statement.Variable> = listOf()

) {

    fun define(variable: Statement.Variable): Environment {
        val newValues = copyAndAdd(variable)

        return Environment(enclosing, newValues)
    }

    fun get(name: String): Statement.Variable {
        val result = values.find { it.identifier == name }

        return result ?: enclosing?.get(name) ?: throw Error("Undefined variable: $name")
    }

    fun assign(name: String, value: Expression): Environment {
        val variable = values.find { it.identifier == name }

        if (variable != null) {
            val newValues = copyAndReplace(Statement.Variable(variable.designation, variable.identifier, value, variable.dataType, variable.position))
            return Environment(enclosing, newValues)
        }
        if (enclosing != null) {
            return Environment(enclosing.assign(name, value), values)
        }

        throw Error("Cannot perform assignation on undefined variable '$name'.")
    }

    fun contains(varName: String): Boolean {
        return values.any { it.identifier == varName }
    }

    private fun copyAndAdd(variable: Statement.Variable): List<Statement.Variable> {
        val newList = values.toMutableList()
        newList.add(variable)
        return newList
    }

    private fun copyAndReplace(newVariable: Statement.Variable): List<Statement.Variable> {
        return values.map {
            if (it.identifier == newVariable.identifier) {
                newVariable
            } else {
                it
            }
        }
    }
}
