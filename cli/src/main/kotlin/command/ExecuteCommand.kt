package command

import cli.FileReader
import interpreter.Interpreter
import lexer.Lexer
import parser.Parser
import position.visitor.Environment

class ExecuteCommand(private val file: String, private val version: String) : Command {
    override fun execute(): String {
        val fileContent = FileReader.getFileContents(file, version)

        if (fileContent.startsWith("Error") || fileContent.startsWith("File not found")) {
            return fileContent
        }

        return try {
            val tokens = Lexer(fileContent, version)

            val asts = Parser(tokens, version)

            val outputBuilder = StringBuilder()
            var currentEnvironment = Environment()

            while (asts.hasNext()) {
                val statement = asts.next()
                val result = Interpreter.interpret(statement, version, currentEnvironment)
                outputBuilder.append(result.first.toString())
                currentEnvironment = result.second
            }

            "${outputBuilder}\nFinished executing $file"
        } catch (e: Exception) {
            "Execution Error: ${e.message}"
        }
    }
}
