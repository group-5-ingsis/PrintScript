import lexer.Lexer
import org.junit.jupiter.api.Test

class LexerTest {

    @Test
    fun printLexResult() {
        val filePath = "src/test/resources/sample.txt"
        val input = TxtConverter.readFileAsString(filePath)
        val result = Lexer.lex(input)
        println(result)
    }
}