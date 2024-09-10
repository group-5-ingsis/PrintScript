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
        val lexer1 = Lexer("if (true) { println(3); }", "1.1")
        val parser1 = Parser(lexer1, "1.1")

        val ast1 = parser1.next()

        val expectedCondition1 = Expression.Literal(true, Position(1, 5))
        val expectedPrint1 = StatementType.Print(Expression.Grouping(Expression.Literal(3, Position(1, 21)), Position(1, 19)), Position(1, 11))
        val expectedBlock1 = StatementType.BlockStatement(Position(1, 19), listOf(expectedPrint1))
        val expectedIf1 = StatementType.IfStatement(Position(1, 10), expectedCondition1, expectedBlock1, null)

        val actualIf1 = ast1 as StatementType.IfStatement

        assertEquals(expectedIf1.condition, actualIf1.condition)
        assertEquals(expectedIf1.thenBranch.statementType, actualIf1.thenBranch.statementType)
        val contentExpectedThenBranch = (expectedIf1.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
        val contentActualThenBranch = (actualIf1.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
        assertEquals(contentExpectedThenBranch.value, contentActualThenBranch.value)
        assertEquals(expectedIf1.elseBranch, actualIf1.elseBranch)

    }



    @Test
    fun testIfElseStm() {
        // Test for if (false) { print(42) } else { print(0) }
        val lexer = Lexer("if (false) { println(42); } else { println(0); }", "1.1")
        val parser = Parser(lexer, "1.1")

        val ast = parser.next()

        val expectedCondition = Expression.Literal(false, Position(1, 5))
        val expectedPrintThen = StatementType.Print(Expression.Grouping(Expression.Literal(42, Position(1, 22)), Position(1, 20)), Position(1, 11))
        val expectedThenBranch = StatementType.BlockStatement(Position(1, 19), listOf(expectedPrintThen))

        val expectedPrintElse = StatementType.Print(Expression.Grouping(Expression.Literal(0, Position(1, 44)), Position(1, 42)), Position(1, 32))
        val expectedElseBranch = StatementType.BlockStatement(Position(1, 44), listOf(expectedPrintElse))

        val expectedIf = StatementType.IfStatement(Position(1, 10), expectedCondition, expectedThenBranch, expectedElseBranch)

        val actualIf = ast as StatementType.IfStatement

        assertEquals(expectedIf.condition, actualIf.condition)
        assertEquals(expectedIf.thenBranch.statementType, actualIf.thenBranch.statementType)
        val expectedThenPrint = (expectedIf.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
        val actualThenPrint = (actualIf.thenBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
        assertEquals(expectedThenPrint.value, actualThenPrint.value)

        val expectedElsePrint = (expectedIf.elseBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
        val actualElsePrint = (actualIf.elseBranch as StatementType.BlockStatement).listStm[0] as StatementType.Print
        assertEquals(expectedElsePrint.value, actualElsePrint.value)
    }

    @Test
    fun testIfWithVariableDeclaration() {
        val lexer = Lexer("let a: Boolean = true; if (a) { let queso: Number = 5; println(queso); }", "1.1")
        val parser = Parser(lexer, "1.1")

        val ast = parser.next()

        val expectedVariableA = StatementType.Variable(
            designation = "let",
            identifier = "a",
            initializer = Expression.Literal(true, Position(1, 19)),
            dataType = "Boolean",
            position = Position(1, 4)
        )

        val expectedCondition = Expression.Variable("a", Position(1, 28))

        val expectedVariableQueso = StatementType.Variable(
            designation = "let",
            identifier = "queso",
            initializer = Expression.Literal(5, Position(1, 51)),
            dataType = "Number",
            position = Position(1, 41)
        )

        val expectedPrint = StatementType.Print(
            Expression.Grouping(Expression.Variable("queso", Position(1, 67)), Position(1, 61)),
            Position(1, 61)
        )

        val expectedBlock = StatementType.BlockStatement(
            position = Position(1, 31),
            listStm = listOf(expectedVariableQueso, expectedPrint)
        )

        val expectedIf = StatementType.IfStatement(
            position = Position(1, 27),
            condition = expectedCondition,
            thenBranch = expectedBlock,
            elseBranch = null
        )

        val actualIf = ast as StatementType.IfStatement

        assertEquals(expectedIf.condition, actualIf.condition)
        assertEquals(expectedIf.thenBranch.statementType, actualIf.thenBranch.statementType)

        val expectedThenBranchList = (expectedIf.thenBranch as StatementType.BlockStatement).listStm
        val actualThenBranchList = (actualIf.thenBranch as StatementType.BlockStatement).listStm

        assertEquals(expectedThenBranchList[0], actualThenBranchList[0]) // Variable queso
        assertEquals(expectedThenBranchList[1], actualThenBranchList[1]) // Print queso
        assertEquals(expectedIf.elseBranch, actualIf.elseBranch)
    }







}