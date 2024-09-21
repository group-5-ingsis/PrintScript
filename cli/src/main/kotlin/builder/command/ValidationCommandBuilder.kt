package builder.command

import builder.CommandBuilder
import command.Command
import command.ValidationCommand

class ValidationCommandBuilder : CommandBuilder {
    override fun build(
        file: String,
        arguments: List<String>,
        version: String
    ): Command {
        return ValidationCommand(file, version)
    }
}
