import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import rules.FormattingRules
import java.nio.file.Paths
import kotlin.test.Test
import kotlin.test.assertEquals

class BracesFormattingTest {

    lateinit var testRules: FormattingRules
    val version = "1.1"

    fun formatCode(input: String): String {
        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val formattedOutput = StringBuilder()
        while (astNodes.hasNext()) {
            val statement = astNodes.next()
            val formattedNode = Formatter.format(statement, testRules, version)
            formattedOutput.append(formattedNode)
        }
        return formattedOutput.toString()
    }

    @Test
    fun testBraceBelowLine() {
        val yamlMapper = YAMLMapper()
        val resource = this::class.java.getResource("/rules/ifBraceDifferentLine.yaml")
            ?: throw IllegalArgumentException("File not found!")
        val file = Paths.get(resource.toURI()).toFile()
        testRules = yamlMapper.readValue(file, FormattingRules::class.java)

        val input = "let something: boolean = true;\n" +
            "if (something)\n" +
            "{\n" +
            "    println(\"Entered if\");\n" +
            "}"
        val expected = "let something: boolean = true;\n" +
            "if (something)\n" +
            "{\n" +
            "    println(\"Entered if\");\n" +
            "}"

        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testBraceSameLine() {
        val yamlMapper = YAMLMapper()
        val resource = this::class.java.getResource("/rules/ifBraceSameLine.yaml")
            ?: throw IllegalArgumentException("File not found!")
        val file = Paths.get(resource.toURI()).toFile()
        testRules = yamlMapper.readValue(file, FormattingRules::class.java)

        val input = "let something: boolean = true;\nif (something){\nprintln(\"Entered if\");\n}"
        val expected = "let something: boolean = true;\nif (something) {\n    println(\"Entered if\");\n}"
        val result = formatCode(input)
        assertEquals(expected, result)
    }
}
