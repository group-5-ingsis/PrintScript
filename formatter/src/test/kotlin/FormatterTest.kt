import formatter.Formatter
import lexer.Lexer
import parser.SyntacticParser
import rules.FormattingRules
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {

    private lateinit var exampleRules: FormattingRules
    private val parser = SyntacticParser()

    @BeforeTest
    fun setUp() {
        exampleRules = FormattingRules(
            spaceBeforeColon = false,
            spaceAfterColon = true,
            spaceAroundAssignment = true,
            newlineBeforePrintln = 2
        )
    }

    @Test
    fun `test assignation formatting`() {
        val badFileContents = javaClass.getResource("/assignation/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/declaration/goodFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }

    @Test
    fun `test assignationDeclaration formatting`() {
        val badFileContents = javaClass.getResource("/assignationDeclaration/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/assignationDeclaration/goodFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }

    @Test
    fun `test declaration formatting`() {
        val badFileContents = javaClass.getResource("/declaration/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/declaration/goodFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }

    @Test
    fun `test methodCall formatting`() {
        val badFileContents = javaClass.getResource("/methodCall/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/methodCall/goodFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }

    @Test
    fun `test operator formatting`() {
        val badFileContents = javaClass.getResource("/binaryOperation/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/binaryOperation/goodFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }
}
