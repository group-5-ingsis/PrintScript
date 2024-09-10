package parser

import lexer.Lexer
import nodes.Expression
import nodes.StatementType
import org.junit.Assert.assertEquals
import org.junit.Test
import position.Position

class ParserV1_1Tester {
    @Test
    fun testIfStm() {
        // Test for if (true) { print(3) }
        val lexer1 = Lexer("if (true) { print(3) }", "1.1")
        val parser1 = Parser(lexer1, "1.1")

        val ast1 = parser1.next()

        val expectedCondition1 = Expression.Literal(true, Position(1, 4))
        val expectedPrint1 = StatementType.Print(Expression.Grouping(Expression.Literal(3, Position(1, 13)), Position(1, 11)), Position(1, 11))
        val expectedBlock1 = StatementType.BlockStatement(Position(1, 8), listOf(expectedPrint1))
        val expectedIf1 = StatementType.IfStatement(Position(1, 1), expectedCondition1, expectedBlock1, null)

        val actualIf1 = ast1 as StatementType.IfStatement

        assertEquals(expectedIf1.condition, actualIf1.condition)
        assertEquals(expectedIf1.thenBranch, actualIf1.thenBranch)
        assertEquals(expectedIf1.elseBranch, actualIf1.elseBranch)
        assertEquals(expectedIf1.position, actualIf1.position)

    }


}