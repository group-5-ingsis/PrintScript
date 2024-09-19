import kotlin.test.Test
import kotlin.test.assertEquals

class SimpleAssignationTest : FormatterTestBase() {

    @Test
    fun testSimpleAssignation() {
        val input = "let a: number = 2;"
        val expected = "let a: number = 2;\n"
        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithBinarySum() {
        val input = "let a: number = 2 + 2;"
        val expected = "let a: number = 2 + 2;\n"
        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithTripleSum() {
        val input = "let a: number = 2 + 2 + 2 * 4;"
        val expected = "let a: number = 2 + 2 + 2 * 4;\n"
        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testAssignationWithComplexOperation() {
        val input = "let a: number = (2 + 2) + 2 * 4;"
        val expected = "let a: number = ( 2 + 2 ) + 2 * 4;\n"
        val result = formatCode(input)
        assertEquals(expected, result)
    }
}
