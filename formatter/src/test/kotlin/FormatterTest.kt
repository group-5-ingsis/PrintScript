import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import formatter.Formatter
import lexer.Lexer
import parser.syntactic.SyntacticParser
import rules.FormattingRules
import java.nio.file.Paths
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {

    private lateinit var exampleRules: FormattingRules
    private val parser = SyntacticParser()

    @BeforeTest
    fun setUp() {
        val yamlMapper = YAMLMapper()

        // Load the file from the classpath
        val resource = this::class.java.getResource("/rules/testRules.yaml")
            ?: throw IllegalArgumentException("File not found!")
        val file = Paths.get(resource.toURI()).toFile()

        // Map the YAML content to FormattingRules class
        exampleRules = yamlMapper.readValue(file, FormattingRules::class.java)
    }

    @Test
    fun `test format`() {
        val badFileContents = javaClass.getResource("/assignation/badFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val goodFileContents = javaClass.getResource("/assignation/goodFormat.ps")?.readText()
            ?: throw IllegalArgumentException("File not found")

        val tokens = Lexer.lex(badFileContents)

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

        val tokens = Lexer.lex(badFileContents)

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

        val tokens = Lexer.lex(badFileContents)

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

        val tokens = Lexer.lex(badFileContents)

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

        val tokens = Lexer.lex(badFileContents)

        val ast = parser.run(tokens)
        val child = ast.getChildren().first()

        val formattedCode = Formatter.format(child, exampleRules)
        assertEquals(goodFileContents, formattedCode)
    }
}
