package nodes

import visitor.NodeVisitor

sealed class StatementType {


  abstract val statementType: String

  fun acceptVisitor(visitor: NodeVisitor) {
    val func = visitor.getVisitorFunctionForStatement(statementType)

    func(this)
  }



  class Print(val value: Expression) : StatementType() {
    override val statementType: String = "PRINT"
  }


  class StatementExpression(val value : Expression) : StatementType(){
    override val statementType: String = "STATEMENT_EXPRESSION"
  }
  /**
   * `Variable` represents a variable declaration assignation or just declaration
   *
   * @param identifier the name of the variable
   * @param initializer the expression assigned to the identifier, if its just declaration, its null
   */
  class Variable(val identifier : String, val initializer: Expression?, val dataType: String) : StatementType(){
    override val statementType: String = "VARIABLE_EXPRESSION"
    init {
        DataTypeManager.checkDataType(dataType)

    }
  }
}
