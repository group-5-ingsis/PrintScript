package visitor

import nodes.Expression
import nodes.StatementType

class  NodeVisitor(val globalScope : Environment) : Visitor {

    private fun evaluateExpression(expr: Expression): Any {
        return expr.acceptVisitor(this)
    }

    private fun evaluateStatement(statement: StatementType){
      return statement.acceptVisitor(this)
    }


    fun interpret(expression: Expression) {
      try {
        val value = evaluateExpression(expression)
        println((value))
      } catch (error: Error) {
        println(error.message)
      }
    }


//  private fun resolveLiteralValue(literal: Expression): Any? {
//    return when (literal.dataType.type) {
//      "NUMBER" -> literal.value.toIntOrNull()
//      "INT" -> literal.value.toIntOrNull() ?: throw IllegalArgumentException("El valor no es un entero válido")
//      "FLOAT" -> literal.value.toFloatOrNull() ?: throw IllegalArgumentException("El valor no es un flotante válido")
//      "LONG" -> literal.value.toLongOrNull() ?: throw IllegalArgumentException("El valor no es un entero largo válido")
//      "SHORT" -> literal.value.toShortOrNull() ?: throw IllegalArgumentException("El valor no es un entero corto válido")
//      "STRING" -> literal.value
//      else -> throw IllegalArgumentException("Tipo de dato no soportado: ${literal.dataType.type}")
//    }
//
//  }
    override fun visitLiteralExp(exp: Expression.Literal): Any? {
      return exp.value
    }

  override fun visitGroupExp(exp: Expression.Grouping): Any? {
    return evaluateExpression(exp.expression)
  }

  override fun visitUnaryExpr(exp: Expression.Unary): Any {
    val rightObject = evaluateExpression(exp.right)
    return when(exp.operator){
      "-" -> - convertToDouble(rightObject)
      "!" -> !isTruthy(rightObject)
      else -> throw IllegalArgumentException("Unsupported types for ${exp.operator} in Unary operation: $rightObject")
    }

  }

  override fun visitBinaryExpr(exp: Expression.Binary): Any {
    val left = evaluateExpression(exp.left)
    val right = evaluateExpression(exp.right)




    fun checkTypesForOperation(left: Any, right: Any): Pair<Any, Any> {
      return when {
        left is Number && right is Number -> Pair(left, right)
        left is String && right is String -> Pair(left, right)
        else -> throw IllegalArgumentException("Unsupported types for ${exp.operator} operation: ${left::class.simpleName} and ${right::class.simpleName}")
      }
    }

    val (leftValue, rightValue) = checkTypesForOperation(left, right)

    return when (exp.operator) {

      "-" -> {
        checkNumberOperands(exp.operator, leftValue, rightValue)
        (leftValue as Double) - (rightValue as Double)
      }
      "/" -> {
        checkNumberOperands(exp.operator, leftValue, rightValue)
        (leftValue as Double) / (rightValue as Double)
      }
      "*" -> {
        checkNumberOperands(exp.operator, leftValue, rightValue)
        (leftValue as Double) * (rightValue as Double)
      }
      "+" -> {
        when {
          leftValue is Double && rightValue is Double -> leftValue + rightValue
          leftValue is String && rightValue is String -> leftValue + rightValue
          else -> throw IllegalArgumentException("Unsupported types for PLUS operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}, must be numbers or strings")
        }
      }

      ">" -> {
        when {
          leftValue is Number && rightValue is Number -> (leftValue.toDouble() > rightValue.toDouble())
          leftValue is String && rightValue is String -> leftValue > rightValue
          else -> throw IllegalArgumentException("Unsupported types for GREATER THAN operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}")
        }
      }
      ">=" -> {
        when {
          leftValue is Number && rightValue is Number -> (leftValue.toDouble() >= rightValue.toDouble())
          leftValue is String && rightValue is String -> leftValue >= rightValue
          else -> throw IllegalArgumentException("Unsupported types for GREATER THAN OR EQUAL operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}")
        }
      }
      "<" -> {
        when {
          leftValue is Number && rightValue is Number -> (leftValue.toDouble() < rightValue.toDouble())
          leftValue is String && rightValue is String -> leftValue < rightValue
          else -> throw IllegalArgumentException("Unsupported types for LESS THAN operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}")
        }
      }
      "<=" -> {
        when {
          leftValue is Number && rightValue is Number -> (leftValue.toDouble() <= rightValue.toDouble())
          leftValue is String && rightValue is String -> leftValue <= rightValue
          else -> throw IllegalArgumentException("Unsupported types for LESS THAN OR EQUAL operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}")
        }
      }
      "==" -> {
        when {
          leftValue is Number && rightValue is Number -> leftValue.toDouble() == rightValue.toDouble()
          leftValue is String && rightValue is String -> leftValue == rightValue
          else -> throw IllegalArgumentException("Unsupported types for EQUAL operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}")
        }
      }
      "!=" -> {
        when {
          leftValue is Number && rightValue is Number -> leftValue.toDouble() != rightValue.toDouble()
          leftValue is String && rightValue is String -> leftValue != rightValue
          else -> throw IllegalArgumentException("Unsupported types for NOT EQUAL operation: ${leftValue::class.simpleName} and ${rightValue::class.simpleName}")
        }
      }

      else -> throw IllegalArgumentException("Unsupported operator: ${exp.operator}")
    }
  }


  override fun getVisitorFunctionForExpression(expressionType: String): (Expression) -> Unit {
    return when (expressionType) {
      "BINARY_EXPRESSION" -> { node ->
        visitBinaryExpr(node as Expression.Binary)
      }

      "GROUPING_EXPRESSION" -> { node ->
        visitGroupExp(node as Expression.Grouping)
      }

      "LITERAL_EXPRESSION" -> { node ->
        visitLiteralExp(node as Expression.Literal)
      }

      "UNARY_EXPRESSION" -> { node ->
        visitUnaryExpr(node as Expression.Unary)
      }
      "IDENTIFIER" -> { node ->
        visitIdentifier(node as Expression.IdentifierExpression)

      }
      else -> {
        TODO("Not implemented yet")
      }
    }
  }

  override fun getVisitorFunctionForStatement(statementType: String): (StatementType) -> Unit {
    return when (statementType) {
      "PRINT" -> { statement ->
        visitPrintStm(statement as StatementType.Print)
      }

      "STATEMENT_EXPRESSION" -> { statement ->
        visitExpressionStm(statement as StatementType.StatementExpression)
      }

      "VARIABLE_EXPRESSION" -> { statement ->
        visitVariableStm(statement as StatementType.Variable)
      }

      else -> {
        TODO("Not implemented yet")
      }
    }
  }

  override fun visitPrintStm(statement: StatementType.Print) {
    val value: Any = evaluateExpression(statement.value)
    println(value)
  }

  override fun visitExpressionStm(statement: StatementType.StatementExpression) {
    evaluateExpression(statement.value)
  }

  override fun visitVariableStm(statement: StatementType.Variable) {
    // remember that if the type is correct is check in other place.

  }


  override fun visitIdentifier(exp: Expression.IdentifierExpression): Any? {
    return globalScope.get(exp.name)
  }


  companion object {
    fun convertToDouble(value: Any): Double {
      return when (value) {
        is Double -> value
        is Float -> value.toDouble()
        is Int -> value.toDouble()
        is Long -> value.toDouble()
        is String -> value.toDoubleOrNull() ?: throw IllegalArgumentException("Cannot convert String to Double: $value")
        else -> throw IllegalArgumentException("Unsupported type: ${value::class.simpleName}")
      }
    }

    private fun isTruthy(thing: Any?): Boolean {
      if (thing == null) return false
      if (thing is Boolean) return thing
      return true
    }


    fun checkNumberOperands(operator: String, left: Any, right: Any) {
      if (left is Number && right is Number) return
      throw RuntimeException("error in operator: $operator , $left and $right  must be numbers.")
    }


  }

}

