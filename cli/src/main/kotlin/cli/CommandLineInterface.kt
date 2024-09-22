package cli

import command.CommandBuilder
import command.analyze.AnalyzeCommandBuilder
import command.execute.ExecuteCommandBuilder
import command.format.FormattingCommandBuilder
import command.validate.ValidationCommandBuilder
import utils.CommandParser
import utils.FileReader

object CommandLineInterface {
    private val commandBuilders: Map<String, CommandBuilder> = getValidBuilders()

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

    private fun getValidBuilders(): Map<String, CommandBuilder> {
        return mapOf(
            "validate" to ValidationCommandBuilder(),
            "command/execute" to ExecuteCommandBuilder(),
            "format" to FormattingCommandBuilder(),
            "command/analyze" to AnalyzeCommandBuilder()
        )
    }
}
