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
        val input = "let a:        number = 2;"
        val version = "1.0"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = "let a: number = 2;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithBinarySum() {
        val input = "let  a: number = 2      +  2;"
        val version = "1.0"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = "let a: number = 2 + 2;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithTripleSum() {
        val input = "let  a: number = 2      +  2+2*4;"
        val version = "1.0"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = "let a: number = 2 + 2 + 2 * 4;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithComplexOperation() {
        val input = "let  a: number = (2      +  2)+ 2*4;"
        val version = "1.0"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = "let a: number = (2 + 2) + 2 * 4;\n"

        assertEquals(expected, result)
    }

    @Test
    fun testPrintLnFormatting() {
        val input = "let a: number = 2; println(a);"
        val version = "1.0"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = "let a: number = 2;\n\n\nprintln(a);\n"
        assertEquals(expected, result)
    }

    @Test
    fun testIfStatementFormatting() {
        val input = "let c : boolean = true; if (c) { let b: number = 3; }"
        val version = "1.1"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        // Assuming the indent is set to 4 spaces in the rules
        val expected = """
        let c: boolean = true;
        if (c) {
            let b: number = 3;
        }
        """.trimIndent()

        assertEquals(expected, result)
    }

    @Test
    fun testIfStatementWithElseFormatting() {
        val input = "let c : boolean = true; if (c) { let b: number = 3; } else { let a: number = 4;};"
        val version = "1.1"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = """
        let c: boolean = true;
        if (c) {
            let b: number = 3;
        } else {
            let a: number = 4;
        }
        """.trimIndent()

        assertEquals(expected, result)
    }

    @Test
    fun testDoubleIfStatementFormatting() {
        val input = "let c : boolean = true; if (c) { if (c) {let a: number = 2;} };"

        val version = "1.1"

        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        val result = Formatter.format(astNodes, exampleRules, version)

        val expected = """
    if (true) {
        if (true) {
            let a: number = 2;
        }
    }
        """.trimIndent()

        assertEquals(expected, result)
    }
}
