package command.validate

import command.Command
import command.CommandBuilder

class ValidationCommandBuilder : CommandBuilder {
  override fun build(
    fileContent: String,
    arguments: List<String>,
    version: String
  ): Command {
    return ValidationCommand(fileContent, version)
  }
}
