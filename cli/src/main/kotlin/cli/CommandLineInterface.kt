package cli

import builder.*
import cli.utils.CommandParser
import cli.utils.FileReader

object CommandLineInterface {
    private val commandBuilders: Map<String, CommandBuilder> = CommandBuilderInitializer.getValidBuilders()

    fun execute(command: String): String {
        val file = CommandParser.getFile(command)
        val operation = CommandParser.getOperation(command)
        val version = CommandParser.getVersion(command)
        val arguments = CommandParser.getArguments(command)

        val fileContent = FileReader.getFileContents(file, version)

        val commandBuilder = commandBuilders[operation] ?: return "Unknown command: $command"

        val command = commandBuilder.build(fileContent, arguments, version)
        val result = command.execute()

        return result
    }
}
