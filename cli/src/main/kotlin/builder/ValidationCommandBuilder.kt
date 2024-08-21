package builder

import command.Command
import command.ValidationCommand

class ValidationCommandBuilder : CommandBuilder {
  override fun build(
    file: String,
    arguments: List<String>,
  ): Command {
    return ValidationCommand(file)
  }
}
