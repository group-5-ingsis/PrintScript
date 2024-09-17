import kotlin.test.Test
import kotlin.test.assertEquals

class IfStatementFormattingTest : FormatterTestBase() {

    @Test
    fun testIfStatementFormatting() {
        val input = "let c: boolean = true; if (c) { let b: number = 3; }"
        val expected = """
        let c: boolean = true;
        if (c) {
            let b: number = 3;
        }
        """.trimIndent()
        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testIfStatementWithElseFormatting() {
        val input = "let c: boolean = true; if (c) { let b: number = 3; } else { let a: number = 4; };"
        val expected = """
        let c: boolean = true;
        if (c) {
            let b: number = 3;
        } else {
            let a: number = 4;
        }
        """.trimIndent()
        val result = formatCode(input)
        assertEquals(expected, result)
    }

    @Test
    fun testDoubleIfStatementFormatting() {
        val input = "let c: boolean = true; if (c) { if (c) {let a: number = 2;} };"
        val expected = """
        if (true) {
            if (true) {
                let a: number = 2;
            }
        }
        """.trimIndent()
        val result = formatCode(input)
        assertEquals(expected, result)
    }
}
