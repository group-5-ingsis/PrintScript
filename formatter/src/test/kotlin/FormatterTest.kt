import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import composite.Node
import formatter.Formatter
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatterTest {

    private lateinit var exampleNode: Node

    @BeforeTest
    fun setUp() {
        exampleNode = Node.Assignation(
            identifier = Node.Identifier("myVar"),
            value = Node.GenericLiteral("42", Node.DataType("NUMBER"))
        )

        YAMLMapper::class.java
    }

    @Test
    fun `test format node`() {
        val rulesFile = File("src/main/resources/rules/testRules.yaml")

        val formattedCode = Formatter.format(exampleNode, rulesFile)

        val expectedOutput = "myVar = 42;\n"

        assertEquals(expectedOutput, formattedCode)
    }
}
