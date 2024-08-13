package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import parser.statement.Statement
import parser.statement.StatementCategorizer
import kotlin.test.assertEquals


class StatementCategoryTest {

    private val categorizer = StatementCategorizer()

    @Test
    fun testAssignationDeclaration(){
        val testString = "let a: Number = 4;"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, "Unknown")

        val statements = listOf(statement)
        val categorizedStatements = categorizer.categorize(statements)
        assertEquals("Declaration", categorizedStatements[0].statementType)
    }

    @Test
    fun unknownStatement(){
        val testString = "let a: Number = 4;"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, "Unknown")

        val statements = listOf(statement)
        val categorizedStatements = categorizer.categorize(statements)
        assertEquals("Declaration", categorizedStatements[0].statementType)
    }


    @Test
    fun testAssignation(){
        val testString = "a = 3"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, "Unknown")


        val statements = listOf(statement)
        val categorizedStatements = categorizer.categorize(statements)
        assertEquals("Assignation", categorizedStatements[0].statementType)
    }
}