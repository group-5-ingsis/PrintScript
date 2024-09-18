// package command
//
// import interpreter.Interpreter
// import lexer.Lexer
// import parser.Parser
// import position.visitor.Environment
// import java.io.BufferedReader
// import java.io.FileReader as JavaFileReader
//
// class ExecuteCommand(
//    private val file: String,
//    private val version: String,
//    private val progressCallback: (Int) -> Unit
// ) : Command {
//
//    override fun execute(): String {
//        val (fileContent, totalCharacters) = readFileWithProgress(file)
//
//        return try {
//            val tokens = Lexer(fileContent, version)
//            val asts = Parser(tokens, version)
//
//            val outputBuilder = StringBuilder()
//            var currentEnvironment = Environment()
//            var charactersRead = 0
//
//            while (asts.hasNext()) {
//                val statement = asts.next()
//                val result = Interpreter.interpret(statement, version, currentEnvironment)
//                outputBuilder.append(result.first.toString())
//                currentEnvironment = result.second
//
//                charactersRead += result.first.toString().length
//                val progress = (charactersRead.toDouble() / totalCharacters * 100).toInt()
//                progressCallback(progress)
//            }
//
//            progressCallback(100)
//            "${outputBuilder}\nFinished executing $file"
//        } catch (e: Exception) {
//            "Execution Error: ${e.message}"
//        }
//    }
//
//    private fun readFileWithProgress(file: String): Pair<String, Int> {
//        val fileReader = JavaFileReader(file)
//        val bufferedReader = BufferedReader(fileReader)
//        val fileContent = StringBuilder()
//        var totalCharacters = 0
//        var charsRead: Int
//        val buffer = CharArray(1024)
//
//        while (bufferedReader.read(buffer).also { charsRead = it } != -1) {
//            fileContent.append(buffer, 0, charsRead)
//            totalCharacters += charsRead
//        }
//        bufferedReader.close()
//
//        return Pair(fileContent.toString(), totalCharacters)
//    }
// }
