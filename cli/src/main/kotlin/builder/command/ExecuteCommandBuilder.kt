package builder.command

import builder.CommandBuilder
import command.Command
import command.ExecuteCommand

class ExecuteCommandBuilder : CommandBuilder {
    override fun build(
        fileContent: String,
        arguments: List<String>,
        version: String
    ): Command {
        return ExecuteCommand(fileContent, version)
    }
}
