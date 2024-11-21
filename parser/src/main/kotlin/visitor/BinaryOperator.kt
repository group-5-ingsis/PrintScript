package visitor

object BinaryOperator {

  fun solveStringAndNumber(left: String, right: Number, operator: String): String {
    return when (operator) {
      "+" -> left.removeSurrounding("\"") + right.toString()
      else -> throw IllegalArgumentException("Unsupported operation between String and Number: $operator")
    }
  }

  fun solveNumberAndString(left: Number, right: String, operator: String): String {
    return when (operator) {
      "+" -> left.toString() + right.removeSurrounding("\"")
      else -> throw IllegalArgumentException("Unsupported operation between Number and String: $operator")
    }
  }

  fun solveNumberAndNumber(left: Number, right: Number, operator: String): Number {
    val leftValue = if (left is Int) left else left.toDouble()
    val rightValue = if (right is Int) right else right.toDouble()

    return when (operator) {
      "+" -> if (leftValue is Int && rightValue is Int) leftValue + rightValue else (leftValue.toDouble() + rightValue.toDouble())
      "-" -> if (leftValue is Int && rightValue is Int) leftValue - rightValue else (leftValue.toDouble() - rightValue.toDouble())
      "*" -> if (leftValue is Int && rightValue is Int) leftValue * rightValue else (leftValue.toDouble() * rightValue.toDouble())
      "/" -> if (leftValue is Int && rightValue is Int) leftValue / rightValue else (leftValue.toDouble() / rightValue.toDouble())
      else -> throw IllegalArgumentException("Unsupported number operation: $operator")
    }
  }

  fun convertToDouble(value: Any?): Double {
    return when (value) {
      is Double -> value
      is Float -> value.toDouble()
      is Int -> value.toDouble()
      is Long -> value.toDouble()
      is String -> value.toDoubleOrNull() ?: throw IllegalArgumentException("Cannot convert String to Double: $value")
      else -> throw IllegalArgumentException("Unsupported type: $value")
    }
  }
}
