import kotlin.test.Test
import kotlin.test.assertEquals

class OperatorFormattingTest : FormatterTestBase() {

    @Test
    fun testEnforceSpaceSurroundingOperations() {
        val input = "let result: number = 5+4*3/2;"
        val expected = "let result: number = 5 + 4 * 3 / 2;\n"
        val result = formatCode(input)
        assertEquals(expected, result)
    }
}
