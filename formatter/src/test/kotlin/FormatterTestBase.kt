import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import formatter.Formatter
import lexer.Lexer
import parser.Parser
import rules.FormattingRules
import java.nio.file.Paths
import kotlin.test.BeforeTest

abstract class FormatterTestBase {

    protected lateinit var exampleRules: FormattingRules
    protected val version = "1.1"

    @BeforeTest
    fun setUp() {
        val yamlMapper = YAMLMapper()
        val resource = this::class.java.getResource("/rules/testRules.yaml")
            ?: throw IllegalArgumentException("File not found!")
        val file = Paths.get(resource.toURI()).toFile()
        exampleRules = yamlMapper.readValue(file, FormattingRules::class.java)
    }

    protected fun formatCode(input: String): String {
        val tokens = Lexer(input, version)
        val astNodes = Parser(tokens, version)
        return Formatter.format(astNodes, exampleRules, version)
    }
}
