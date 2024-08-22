package visitor

import Node

object BinaryEvaluator {
  fun evaluate(operation: Node.BinaryOperations): Any {
    val leftValue = resolveOperandValue(operation.left)
    val rightValue = resolveOperandValue(operation.right)

    val operationSymbol = operation.symbol
    return performOperation(leftValue, rightValue, operationSymbol)
  }

  private fun resolveOperandValue(operand: Node): Any? {
    return when (operand) {
      is Node.GenericLiteral -> resolveLiteralValue(operand)
      is Node.Identifier -> resolveIdentifierValue(operand)
      is Node.BinaryOperations -> evaluate(operand)
      else -> throw Exception("Unsupported type in binary operation: ${operand.nodeType}")
    }
  }

  private fun resolveLiteralValue(literal: Node.GenericLiteral): Any? {
    return if (literal.dataType.type != "STRING") {
      literal.value.toIntOrNull()
    } else {
      literal.value
    }
  }

  private fun resolveIdentifierValue(identifier: Node.Identifier): Any? {
    return VariableTable.getVariable(identifier.value)
  }

  private fun performOperation(
    leftValue: Any?,
    rightValue: Any?,
    operator: String,
  ): Any {
    val leftIsNumber = leftValue is Number
    val rightIsNumber = rightValue is Number

    return when (operator) {
      "+" -> performAddition(leftValue, rightValue, leftIsNumber, rightIsNumber)
      "-" -> performSubtraction(leftValue, rightValue, leftIsNumber, rightIsNumber)
      "*" -> performMultiplication(leftValue, rightValue, leftIsNumber, rightIsNumber)
      "/" -> performDivision(leftValue, rightValue, leftIsNumber, rightIsNumber)
      else -> throw Exception("Unsupported binary operator: $operator")
    }
  }

  private fun performAddition(
    leftValue: Any?,
    rightValue: Any?,
    leftIsNumber: Boolean,
    rightIsNumber: Boolean,
  ): Any {
    return when {
      leftValue is String && rightValue is String -> leftValue + rightValue
      leftIsNumber && rightIsNumber -> (leftValue as Number).toDouble() + (rightValue as Number).toDouble()
      leftValue is String || rightValue is String -> leftValue.toString() + rightValue.toString()
      else -> throw Exception("Unsupported operands for addition")
    }
  }

  private fun performSubtraction(
    leftValue: Any?,
    rightValue: Any?,
    leftIsNumber: Boolean,
    rightIsNumber: Boolean,
  ): Any {
    if (leftIsNumber && rightIsNumber) {
      return (leftValue as Number).toDouble() - (rightValue as Number).toDouble()
    } else {
      throw Exception("Subtraction requires both operands to be numbers")
    }
  }

  private fun performMultiplication(
    leftValue: Any?,
    rightValue: Any?,
    leftIsNumber: Boolean,
    rightIsNumber: Boolean,
  ): Any {
    if (leftIsNumber && rightIsNumber) {
      return (leftValue as Number).toDouble() * (rightValue as Number).toDouble()
    } else {
      throw Exception("Multiplication requires both operands to be numbers")
    }
  }

  private fun performDivision(
    leftValue: Any?,
    rightValue: Any?,
    leftIsNumber: Boolean,
    rightIsNumber: Boolean,
  ): Any {
    if (leftIsNumber && rightIsNumber) {
      return (leftValue as Number).toDouble() / (rightValue as Number).toDouble()
    } else {
      throw Exception("Division requires both operands to be numbers")
    }
  }
}
