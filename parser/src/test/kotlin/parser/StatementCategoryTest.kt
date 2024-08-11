package parser

import lexer.Lexer
import org.junit.jupiter.api.Test
import parser.statement.Statement
import parser.statement.StatementType


class StatementCategoryTest {



    @Test
    fun testAssignationDeclaration(){
        val testString = "let a: Number = 4;"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, "Unknown")

        val statements = listOf(statement)
        val categorizedStatements = StatementType.categorize(statements)
        println(categorizedStatements)
    }

    @Test
    fun testAssignation(){
        val testString = "a = 3"
        val tokens = Lexer.lex(testString, listOf())

        val statement = Statement(tokens, "Unknown")


        val statements = listOf(statement)
        val categorizedStatements = StatementType.categorize(statements)
        println(categorizedStatements)
    }
}