import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import composite.Node
import formatter.Formatter
import rules.FormattingRules
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {

    private lateinit var formattingRules: FormattingRules
    private lateinit var exampleNode: Node

    @BeforeTest
    fun setUp() {
        formattingRules = FormattingRules(
            spaceBeforeColon = true,
            spaceAfterColon = true,
            spaceAroundAssignment = true,
            newlineBeforePrintln = 1
        )

        exampleNode = Node.Assignation(
            identifier = Node.Identifier("myVar"),
            value = Node.GenericLiteral("42", Node.DataType("NUMBER"))
        )

        YAMLMapper::class.java
    }

    @Test
    fun `test format node`() {
        val formattedCode = Formatter.format(exampleNode, "rules/testRules.yaml")

        val expectedOutput = "myVar = 42;\n" // Adjust according to your expected format

        assertEquals(expectedOutput, formattedCode)
    }
}
