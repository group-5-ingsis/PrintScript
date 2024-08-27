import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
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

        YAMLMapper::class.java
    }

    @Test
    fun `test format`() {
        val badFileContents = javaClass.getResource("/badFormatting.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/goodFormatting.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents, listOf())

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)

        assertEquals(goodFileContents, formattedCode)
    }
}
