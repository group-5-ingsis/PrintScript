package parser


import UnknownExpressionException
import exceptions.BadSyntacticException
import nodes.DataTypeManager
import nodes.Expression
import nodes.StatementType
import position.Position
import token.Token
import visitor.NodeVisitor


class SyntacticParser(private val tokens : List<Token>) {


    private var current = 0 // The next token of the interpreter to see

    // mainFunction (The program it-self):
    fun parse() : RootNode {
        val node = RootNode.create()

        while (isNotAtEndOfTokens()){
            node.addChild(declarationAssignation())

        }
        return node

    }



  /**
   * DIFFERENT KIND OF EXPRESSIONS
   *
   *   expression     → assigment ;
   *   assigment      → IDENTIFIER "=" assignment
   *                | equality ;
   *   equality       → comparison ( ( "!=" | "==" ) comparison )* ;
   *   comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
   *   term           → factor ( ( "-" | "+" ) factor )* ;
   *   factor         → unary ( ( "/" | "*" ) unary )* ;
   *   unary          → ( "!" | "-" ) unary
   *   | primary ;
   *   primary        → NUMBER | STRING | "true" | "false" | "null"
   *   | "(" expression ")" | IDENTIFIER ;
   *
   */



  private fun expression(): Expression {
    return assigment()
  }

    private fun assigment(): Expression{
       val exp =  equality()

        if (peek().value == "="){

            val position = getPosition()
            consumeTokenValue("=")
            if (exp is Expression.Variable){
                // use case example ->  newPoint(x + 2, 0).y = 3;
                // this should be a Variable:  newPoint(x + 2, 0).y

                val name = expression()

                return Expression.Assign(exp.name, name, position)
            }
            throw BadSyntacticException("Invalid assignment target. at: " + position.line + " line, and: " + position.symbolIndex + "column")

        }
        return exp

    }


  private fun equality() : Expression {
      var expression: Expression = comparison() // Any valor comparable

    while(isBangEqualOrEqualEqual()){
      val position = getPosition()
      val tokenOperator = peek().value

      advance()

      val rightExpression = comparison()
      expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
    }
    return expression


  }


  private fun comparison() : Expression {
    var expression: Expression = term() // Any valor comparable

    while(peek().type == "COMPARISON"){
        val position = getPosition()
      val tokenOperator = peek().value

      advance()

      val rightExpression = comparison()

      expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
    }
    return expression
  }


  private fun term(): Expression {
    var expression = factor()

    while(isMinusOrPlus()){
      val tokenOperator = peek().value
        val position = getPosition()
      advance()

      val rightExpression = factor()

      expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
    }
    return expression

  }


  private fun factor() : Expression {
    var expression = unary()

    while(isMultiplicationOrDivision()){
      val tokenOperator = peek().value
        val position = getPosition()
      advance()

      val rightExpression = unary()

      expression = Expression.Binary(expression, tokenOperator, rightExpression, position)
    }
    return expression
  }


  private fun unary() : Expression {
    if (peek().value == "!") {// TODO(replace for token type!!!!) TODO(Check if works with negative numbers, if not, use it too with "-")
      val tokenOperator = peek().value
        val position = getPosition()
      advance()
      val rightExpression = unary()
      return Expression.Unary(tokenOperator, rightExpression, position)

    }
    if (peek().value == "-"){
        val position = getPosition()
      val tokenOperator = peek().value
      advance()
      val rightExpression = unary()
      return Expression.Unary(tokenOperator, rightExpression, position)
    }

    return primary()



  }

  private fun primary(): Expression {
      val position = getPosition()
      if (isType("BOOLEAN")) {

      advance()
      return Expression.Literal(previous().value.toBoolean(), position)

    }else if (isType("NULL")){
      advance()
      return Expression.Literal(null, position)
    }else if (checkTokensAreFromSomeTypes(listOf("NUMBER", "STRING"))){
      advance()
      return Expression.Literal(getPreviousLiteral(), position)
    }else if (peek().value == "("){
      advance()
      val expr = expression()
      consumeTokenValue(")")
      return Expression.Grouping(expr, position)
    } else if (isType("IDENTIFIER")){
            val idem = advance().value
          return Expression.Variable(idem, position)
    }

    throw UnknownExpressionException(peek())

  }

  /**
   * DIFFERENT KIND OF STATEMENTS:
   *
   * program        →  declaration* EOF ;
   *
   * declaration    → varDecl
   *                | statement ; // later here: functions, and classes
   *
   * statement      → exprStmt
   *                | printStmt ;
   *
   * exprStmt       → expression ";" ;
   * printStmt      → "print" expression ";" ;
   * varDecl        → "let" IDENTIFIER VARIABLE_TYPE ( "=" expression )? ";" ;
   *
   */




  private fun statement(): StatementType {

    if (isType("PRINT")) return printStatement()

    return expressionStatement()
  }


  private fun printStatement() : StatementType {
      val position = getPosition()
    advance()
      val value: Expression = expression()
    consumeTokenValue(";")
      return if (value is Expression.Grouping){
          StatementType.Print(value, position)
      }else {
          throw Error("Expression shoud be groupping at line : " + position.line)
      }

  }


  private fun expressionStatement(): StatementType {
      val position = getPosition()
    val expr: Expression = expression()
    consumeTokenValue(";")
    return StatementType.StatementExpression(expr, position)
  }


  private fun declarationAssignation(): StatementType {
    if (isType("DECLARATION_KEYWORD") ){
      advance()
      return letDeclaration()
    }

    if (isType("CONST")){
      advance()
      return constDeclaration()
    }

    return statement()

  }



  private fun constDeclaration(): StatementType {
      val position = getPosition()
    val identifier = consumeTokenName("IDENTIFIER")
    consumeTokenValue(":")
    val dataType = consumeTokenName("VARIABLE_TYPE").value
    consumeTokenValue("=")
    val initializer = expression()

    return StatementType.Variable("const", identifier.value, initializer, dataType, position)
  }

  private fun letDeclaration(): StatementType {
      val position = getPosition()
    // a : Number =
    var initializer : Expression? = null
    val identifier = consumeTokenName("IDENTIFIER")
    consumeTokenValue(":")
    val dataType = consumeTokenName("VARIABLE_TYPE").value
    if (peek().value == "=") {

      consumeTokenValue("=")
      initializer = expression()
      DataTypeManager.checkDataTypeIsOkWithExpression(initializer, dataType)
    }

    consumeTokenValue(";")


    return StatementType.Variable("let", identifier.value, initializer, dataType, position)

  }


  /**
   * OPERATIVE FUNCTIONS
   * Different types of functions that makes small operations and manipulate the index of the list of tokens
   *
   *  */

  private fun consumeTokenValue(value: String): Token {
    if (peek().value == value) return advance()

    throw BadSyntacticException("Expect: $value after expression.")
  }
  private fun consumeTokenName(type: String): Token {
    if (peek().type == type) return advance()

    throw BadSyntacticException("Expect this type: $type")
  }

  private fun previous(): Token {
    return tokens[current - 1]
  }

  private fun peek(): Token {
    return tokens[current]
  }
  private fun isType(type: String): Boolean {
      return (peek().type == type)
  }

  private fun checkTokensAreFromSomeTypes(types: List<String>): Boolean {
    return if (isNotAtEndOfTokens()) {

      types.any { type -> tokens[current].type == type }

    } else {

      false

    }
  }
  private fun isNotAtEndOfTokens(): Boolean {
    return current < tokens.size -1
  }

  private fun isNotAtEndOfPhrase(): Boolean {
    if (isNotAtEndOfTokens()){
      if (tokens[current].value == ";"){
        return false
      }

    }

    return true
  }


  private fun advance(): Token {
    if (isNotAtEndOfTokens()) current++
    return previous()
  }

  private fun getPreviousLiteral() : Any {
      // Only works for strings and numbers
    val previous = previous()
    return if (previous.type == "STRING"){
      previous.value
    }else {
      if (previous.value.contains('.')) {
        previous.value.toDouble()
      } else {
        previous.value.toInt()
      }
    }

  }


  //All equals, then replace for token Types
  // TODO(Replace for token types)
  private fun isBangEqualOrEqualEqual(): Boolean{
    return if (isNotAtEndOfTokens()){
      (peek().value == "==" || peek().value == "!=")
    }else false
  }
  //All equals, then replace for token Types
  // TODO(Replace for token types)
  private fun isMinusOrPlus(): Boolean{
    return if (isNotAtEndOfTokens()){
      (peek().value == "+" || peek().value == "-")
    }else false
  }
  //All equals, then replace for token Types
  // TODO(Replace for token types)
  private fun isMultiplicationOrDivision(): Boolean{
    return if (isNotAtEndOfTokens()){
      (peek().value == "/" || peek().value == "*")

    }else false

  }
  private fun getPosition(): Position {

      return tokens[current].position
  }


    class RootNode private constructor() {
    private val children = mutableListOf<StatementType>()

    fun addChild(child: StatementType) {
      children.add(child)
    }

    fun getChildren(): List<StatementType> {
      return children
    }

    companion object {
      internal fun create(): RootNode {
        return RootNode()
      }
    }

    fun accept(visitor: NodeVisitor) {
      for (child in children) {
        child.acceptVisitor(visitor)
      }
    }


  }
}
