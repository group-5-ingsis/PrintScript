package environment

import nodes.Expression
import nodes.StatementType
import visitor.ExpressionVisitor

class Environment(

  val enclosing: Environment? = null,
  private val values: List<StatementType.Variable> = listOf()

) {

  // Define a new variable in the environment
  fun define(variable: StatementType.Variable): Environment {
    val newValues = copyAndAdd(variable)

    return Environment(enclosing, newValues)
  }

  // Retrieve the variable's value by name
  fun get(name: String): StatementType.Variable {
    val result = values.find { it.identifier == name }

    return result ?: enclosing?.get(name) ?: throw Error("Undefined variable: $name")
  }

  fun getValue(name: String): Any? {
    val variable = get(name)
    val initializer = variable.initializer ?: return null
    val expressionVisitor = ExpressionVisitor()
    val result = initializer.acceptVisitor(expressionVisitor, this)
    return result.first
  }

  // Assign a new value to a variable
  fun assign(name: String, value: Expression): Environment {
    val variable = values.find { it.identifier == name }

    if (variable != null) {
      val newValues = copyAndReplace(StatementType.Variable(variable.designation, variable.identifier, value, variable.dataType, variable.position))
      return Environment(enclosing, newValues)
    }
    if (enclosing != null) {
      return Environment(enclosing.assign(name, value), values)
    }

    throw Error("Cannot perform assignation on undefined variable '$name'.")
  }

  // Method to get the value of a variable using the ExpressionVisitor
  fun getValue(variableName: String, visitor: ExpressionVisitor): Any? {
    val variable = get(variableName)
    return variable.initializer?.let { visitor.evaluateExpression(it, this).first }
  }

  // Method to check if the environment contains a variable
  fun contains(varName: String): Boolean {
    return values.any { it.identifier == varName }
  }

  private fun copyAndAdd(variable: StatementType.Variable): List<StatementType.Variable> {
    val newList = values.toMutableList()
    newList.add(variable)
    return newList
  }

  private fun copyAndReplace(newVariable: StatementType.Variable): List<StatementType.Variable> {
    return values.map {
      if (it.identifier == newVariable.identifier) {
        newVariable
      } else {
        it
      }
    }
  }
}
