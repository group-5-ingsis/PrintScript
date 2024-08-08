package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import parser.statement.Statement
import parser.statement.UnknownStatement

class StatementCategoryTest {

    private val categorizer: StatementCategorizer = StatementCategorizer()

    @Test
    fun testAssignationType(){
        val testString = "let a: number = 4"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, UnknownStatement())


        val statements = listOf(statement)
        val categorizedStatements = categorizer.categorize(statements)
        println(categorizedStatements)
    }
}