package utils

object BinaryOperator {

    fun solveStringAndString(left: String, right: String, operator: String): String {
        return when (operator) {
            "+" -> left + right
            else -> throw IllegalArgumentException("Unsupported string operation: $operator")
        }
    }

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
        return when (operator) {
            "+" -> left.toDouble() + right.toDouble()
            "-" -> left.toDouble() - right.toDouble()
            "*" -> left.toDouble() * right.toDouble()
            "/" -> left.toDouble() / right.toDouble()
            else -> throw IllegalArgumentException("Unsupported number operation: $operator")
        }
    }
}
