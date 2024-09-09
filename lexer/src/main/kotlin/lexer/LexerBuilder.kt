package lexer

class LexerBuilder {
    private var input: String = ""
    private var version: String = "1.1"

    fun withInput(input: String): LexerBuilder {
        this.input = input
        return this
    }

    fun withVersion(version: String): LexerBuilder {
        this.version = version
        return this
    }

    fun build(): Lexer {
        return Lexer(input, version)
    }
}
