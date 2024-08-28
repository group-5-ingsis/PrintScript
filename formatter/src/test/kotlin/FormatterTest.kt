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
            spaceBeforeColon = true,
            spaceAfterColon = false,
            spaceAroundAssignment = true,
            newlineBeforePrintln = 1
        )
    }

    @Test
    fun `test assignation formatting`() {
        val badFileContents = javaClass.getResource("/assignation/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/declaration/goodFormatting.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }
}
