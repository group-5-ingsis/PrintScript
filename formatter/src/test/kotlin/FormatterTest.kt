import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import rules.FormattingRules
import java.nio.file.Paths
import kotlin.test.*

class FormatterTest {

    private lateinit var exampleRules: FormattingRules

    @BeforeTest
    fun setUp() {
        val yamlMapper = YAMLMapper()

        val resource = this::class.java.getResource("/rules/testRules.yaml")
            ?: throw IllegalArgumentException("File not found!")
        val file = Paths.get(resource.toURI()).toFile()

        exampleRules = yamlMapper.readValue(file, FormattingRules::class.java)
    }

    @Test
    fun testSimpleAssignation() {
        val input = "let a:        Number = 2;"

        val tokens = Lexer(input)
        val astNodes = Parser(tokens)
        val formatter = Formatter(astNodes)

        val result = formatter.format(exampleRules)

        val expected = "let a: Number = 2;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithBinarySum() {
        val input = "let  a: Number = 2      +  2;"

        val tokens = Lexer(input)
        val astNodes = Parser(tokens)
        val formatter = Formatter(astNodes)

        val result = formatter.format(exampleRules)

        val expected = "let a: Number = 2 + 2;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithTripleSum() {
        val input = "let  a: Number = 2      +  2+2*4;"

        val tokens = Lexer(input)
        val astNodes = Parser(tokens)
        val formatter = Formatter(astNodes)

        val result = formatter.format(exampleRules)

        val expected = "let a: Number = 2 + 2 + 2 * 4;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithComplexOperation() {
        val input = "let  a: Number = (2      +  2)+ 2*4;"

        val tokens = Lexer(input)
        val astNodes = Parser(tokens)
        val formatter = Formatter(astNodes)

        val result = formatter.format(exampleRules)

        val expected = "let a: Number = (2 + 2) + 2 * 4;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testPrintLnFormatting() {
        val input = "let a: Number = 2; println(a);"

        val tokens = Lexer(input)
        val astNodes = Parser(tokens)
        val formatter = Formatter(astNodes)

        val result = formatter.format(exampleRules)

        val expected = "let a: Number = 2;\n\n\nprintln(a);\n"
        assertEquals(expected, result)
    }
}
