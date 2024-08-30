package command

import cli.FileReader

class ComandFormater(private val file: String, private val version: String) : Command {
    val fileContent = FileReader.getFileContents(file, version)
    override fun execute(): String {
        TODO("Not yet implemented")
    }
}
