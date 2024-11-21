import kotlin.test.Test
import kotlin.test.assertEquals

class PrintStatementFormattingTest : FormatterTestBase() {

  @Test
  fun testPrintLnFormatting() {
    val input = "let a: number = 2; println(a);"
    val expected = "let a: number = 2;\nprintln ( a );\n\n\n"
    val result = formatCode(input)
    assertEquals(expected, result)
  }

  @Test
  fun testSingleSpaceSeparationPrintln() {
    val input = "println(2);"
    val expected = "println ( 2 );\n\n\n"
    val result = formatCode(input)
    assertEquals(expected, result)
  }

  @Test
  fun test2LineBreaksAfterPrintln() {
    val input = "let something:string = \"a really cool thing\";\n" +
      "println (something) ;\n" +
      "println(\"in the way she moves\");"
    val expected = "let something: string = \"a really cool thing\";\n" +
      "println ( something );\n" +
      "\n" +
      "\n" +
      "println ( \"in the way she moves\" );\n\n\n"
    val result = formatCode(input)
    assertEquals(expected, result)
  }
}
