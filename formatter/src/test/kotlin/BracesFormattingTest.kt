import kotlin.test.Test
import kotlin.test.assertEquals

class BracesFormattingTest : FormatterTestBase() {

    @Test
    fun testBraceBelowLine() {
        val input = "let something: boolean = true;\n" +
            "if (something)\n" +
            "{\n" +
            "    println(\"Entered if\");\n" +
            "}"
        val expected = "let something: boolean = true;\n" +
            "if (something)\n" +
            "{\n" +
            "  println(\"Entered if\");\n" +
            "}"
        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testBraceSameLine() {
        val input = "let something: boolean = true;\nif (something){\nprintln(\"Entered if\");\n}"
        val expected = "let something: boolean = true;\nif (something) {\n  println(\"Entered if\");\n}"
        val result = formatCode(input)
        assertEquals(expected, result)
    }
}
