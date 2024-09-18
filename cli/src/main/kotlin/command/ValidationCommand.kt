// package command
//
// import cli.FileReader
// import lexer.Lexer
// import parser.Parser
//
// class ValidationCommand(private val file: String, private val version: String) : Command {
//    override fun execute(): String {
//        val fileContent = FileReader.getFileContents(file, version)
//
//        if (fileContent.startsWith("Error") || fileContent.startsWith("File not found")) {
//            return fileContent
//        }
//
//        return try {
//            val tokens = Lexer(fileContent)
//            Parser(tokens)
//
//            "File Validated"
//        } catch (e: Exception) {
//            "Validation error: ${e.message}"
//        }
//    }
// }
