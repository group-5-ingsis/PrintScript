package command

class AnalyzeCommand(private val file: String, private val version: String, private val rulesFile: String) : Command {

    override fun execute(): String {
//        val fileContent = FileReader.getFileContents(file, version)
//
//        val linterRules = FileReader.getFormattingRules(rulesFile, version)
//
//        return try {
//            val tokens = Lexer.lex(fileContent)
//
//            val ast = Parser().run(tokens)
//
//            Interpreter.interpret(ast)
//
//            val linter = Linter()
//            linter.lint(ast)
//
//            "File with no problems"
//        } catch (e: Exception) {
//            "Validation error: ${e.message}"
//        }
        return "Need More Coverage"
    }
}