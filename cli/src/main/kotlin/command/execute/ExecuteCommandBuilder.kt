package command.execute

import command.Command
import command.CommandBuilder

class ExecuteCommandBuilder : CommandBuilder {
    override fun build(
        fileContent: String,
        arguments: List<String>,
        version: String
    ): Command {
        return ExecuteCommand(fileContent, version)
    }
}
