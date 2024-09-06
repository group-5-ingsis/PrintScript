import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import rules.FormattingRules
import java.nio.file.Paths
import kotlin.test.BeforeTest
import kotlin.test.Test

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
    fun testFormatting() {
        val input = "let a:        Number = 2;"

        val tokens = Lexer(input)
        val astNodes = Parser(tokens)
        val formatter = Formatter(astNodes)

        val result = formatter.format(exampleRules)

        println(result)
    }
}
