package visitor

import environment.Environment
import nodes.Expression
import nodes.Type
import position.visitor.VisitorResultExpressions
import token.Position

class ExpressionVisitor(private val inputProvider: InputProvider = PrintScriptInputProvider()) {

  fun evaluateExpression(expr: Expression, environment: Environment): VisitorResultExpressions {
    return expr.acceptVisitor(this, environment)
  }

  fun visitLiteralExp(exp: Expression.Literal, environment: Environment): VisitorResultExpressions {
    return Pair(exp.value, environment)
  }
  fun visitGroupExp(exp: Expression.Grouping, environment: Environment): VisitorResultExpressions {
    return evaluateExpression(exp.expression, environment)
  }
  fun visitUnaryExpr(exp: Expression.Unary, environment: Environment): Pair<Any?, Environment> {
    val rightObject = evaluateExpression(exp.right, environment)
    return when (exp.operator) {
      "-" -> {
        val result = -BinaryOperator.convertToDouble(rightObject)
        Pair(result, environment)
      }
      else -> throw IllegalArgumentException("Unsupported types for ${exp.operator} in Unary operation: $rightObject")
    }
  }

  fun visitBinaryExpr(exp: Expression.Binary, scope: Environment): Pair<Any?, Environment> {
    val (left, leftScope) = evaluateExpression(exp.left, scope)
    val (right, rightScope) = evaluateExpression(exp.right, leftScope)

    return when {
      (left is Number && right is Number) -> BinaryOperator.solveNumberAndNumber(left, right, exp.operator) to rightScope
      (left is String && right is String) -> solveStringAndString(left, right, exp.operator) to rightScope
      (left is String && right is Number) -> BinaryOperator.solveStringAndNumber(left, right, exp.operator) to rightScope
      (left is Number && right is String) -> BinaryOperator.solveNumberAndString(left, right, exp.operator) to rightScope
      else -> throw IllegalArgumentException("Unsupported operand types: ${left!!::class} and ${right!!::class}")
    }
  }

  private fun solveStringAndString(left: String, right: String, operator: String): String {
    return when (operator) {
      "+" -> left + right
      else -> throw IllegalArgumentException("Unsupported string operation: $operator")
    }
  }

  fun visitVariableExp(exp: Expression.Variable, scope: Environment): VisitorResultExpressions {
    val expressionName = exp.name
    val name = scope.getValue(expressionName)

    return Pair(name, scope)
  }

  fun visitAssignExpr(exp: Expression.Assign, scope: Environment): VisitorResultExpressions {
    val (value, newScope) = evaluateExpression(exp.value, scope)
    val newScope2 = newScope.assign(exp.name, exp.value)
    return Pair(value, newScope2)
  }

  fun visitReadInput(expr: Expression.ReadInput, env: Environment): VisitorResultExpressions {
    val solved = evaluateExpression(expr.value, env)
    val env2 = solved.second

    val inputText = solved.first

    if (inputText !is String) throw IllegalArgumentException("Input Text must be a string in : " + expr.position.toString())

    val input = inputProvider.input(inputText)

    val convertedInput: Any = convertInput(expr.valueShouldBeOfType, input, expr.position)

    return Pair(convertedInput, env2)
  }

  fun convertInput(type: Type, input: String, position: Position): Any {
    return when (type) {
      Type.BOOLEAN -> input.toBooleanStrictOrNull()
        ?: throw IllegalArgumentException("Expected a Boolean but got: $input at $position")

      Type.NUMBER -> if (input.contains(".")) {
        input.toDoubleOrNull()
          ?: throw IllegalArgumentException("Expected a Number but got: $input at $position")
      } else {
        input.toIntOrNull()
          ?: throw IllegalArgumentException("Expected an Integer but got: $input at $position")
      }

      Type.STRING -> input

      Type.ANY -> input
    }
  }

  fun visitReadEnv(expr: Expression.ReadEnv, env: Environment): VisitorResultExpressions {
    val solved = evaluateExpression(expr.value, env)
    val env2 = solved.second

    val variableName = solved.first

    if (variableName !is String) throw IllegalArgumentException("Input Text must be a string in : " + expr.position.toString())

    val value = env2.getValue(variableName)

    val envValue = convertInput(expr.valueShouldBeOfType, value.toString(), expr.position)

    return Pair(envValue, env2)
  }

  fun visitIdentifierExp(exp: Expression.IdentifierExpression, environment: Environment): VisitorResultExpressions {
    return Pair(exp, environment)
  }
}
