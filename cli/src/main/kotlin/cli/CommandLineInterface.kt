package cli

import builder.*
import cli.utils.CommandParser

object CommandLineInterface {
    private val commandBuilders: Map<String, CommandBuilder> = CommandBuilderInitializer.getValidBuilders()

    fun execute(command: String): String {
        val file = CommandParser.getFile(command)
        val operation = CommandParser.getOperation(command)
        val version = CommandParser.getVersion(command)
        val arguments = CommandParser.getArguments(command)

        val commandBuilder = commandBuilders[operation] ?: return "Unknown command: $command"

        val command = commandBuilder.build(file, arguments, version)
        val result = command.execute()

        return result
    }
}
