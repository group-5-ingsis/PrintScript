package cli

import command.CommandBuilder
import command.analyze.AnalyzeCommandBuilder
import command.execute.ExecuteCommandBuilder
import command.format.FormatCommandBuilder
import command.validate.ValidationCommandBuilder
import utils.CommandParser

object CommandLineInterface {
  private val commandBuilders: Map<String, CommandBuilder> = getValidBuilders()

  fun execute(command: String): String {
    val file = CommandParser.getFile(command)
    val operation = CommandParser.getOperation(command)
    val version = CommandParser.getVersion(command)
    val arguments = CommandParser.getArguments(command)

    val commandBuilder = commandBuilders[operation] ?: return "Unknown command: $command"

    val commandToExecute = commandBuilder.build(file, arguments, version)
    val result = commandToExecute.execute()

    return result
  }

  private fun getValidBuilders(): Map<String, CommandBuilder> {
    return mapOf(
      "validate" to ValidationCommandBuilder(),
      "execute" to ExecuteCommandBuilder(),
      "format" to FormatCommandBuilder(),
      "analyze" to AnalyzeCommandBuilder()
    )
  }
}
