package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import parser.statement.Statement
import parser.statement.UnknownStatement

class StatementCategoryTest {

    private val categorizer: StatementCategorizer = StatementCategorizer()

    @Test
    fun testAssignationDeclaration(){
        val testString = "let a: number = 4"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, UnknownStatement())


        val statements = listOf(statement)
        val categorizedStatements = categorizer.categorize(statements)
        println(categorizedStatements)
    }

    @Test
    fun testAssignation(){
        val testString = "a = 3"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, UnknownStatement())


        val statements = listOf(statement)
        val categorizedStatements = categorizer.categorize(statements)
        println(categorizedStatements)
    }
}