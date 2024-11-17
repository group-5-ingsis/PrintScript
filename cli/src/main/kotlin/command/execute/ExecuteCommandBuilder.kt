package command.execute

import command.Command
import command.CommandBuilder
import utils.FileReader

class ExecuteCommandBuilder : CommandBuilder {
  override fun build(
    file: String,
    arguments: List<String>,
    version: String
  ): Command {
    val fileContent = FileReader.getFileContents(file, version)
    return ExecuteCommand(fileContent, version)
  }
}
