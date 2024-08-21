package command

import cli.FileReader

class ValidationCommand(private val fileLocation: String): Command {

  override fun execute(): String {
    val fileContent = FileReader.getFileContents(fileLocation)


  }



}
