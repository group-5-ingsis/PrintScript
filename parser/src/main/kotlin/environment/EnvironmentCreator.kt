package environment

import nodes.Expression
import nodes.Statement
import token.Position

object EnvironmentCreator {

    fun create(envVars: Map<String, String>): Environment {
        var environment = Environment()
        for ((key, value) in envVars) {
            val variable = Statement.Variable(
                designation = "const",
                identifier = key,
                initializer = Expression.Literal(value, Position(0, 0)),
                dataType = getDataType(value),
                position = Position(0, 0)
            )
            environment = environment.define(variable)
        }
        return environment
    }

    private fun getDataType(value: String): String {
        return when {
            value.equals("true", ignoreCase = true) || value.equals("false", ignoreCase = true) -> "boolean"
            value.toIntOrNull() != null -> "number"
            else -> "string"
        }
    }
}
