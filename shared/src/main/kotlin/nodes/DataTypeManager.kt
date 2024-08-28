package nodes

object DataTypeManager {
  private val listOfAllowDataTypes = listOf(
    "String",
    "Number",
    "Int",
    "Float",
    "Double",
  )

  fun checkDataType(dataType: String) {
    if (!listOfAllowDataTypes.contains(dataType)) {
      throw IllegalArgumentException("$dataType is not allowed")
    }
  }

  fun checkDataTypeIsOkWithExpression(exp: Expression, dataType: String) {

    // TODO(How to check that expression is of x type???)

  }



}
