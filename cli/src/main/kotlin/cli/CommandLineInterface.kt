package cli

import builder.CommandBuilder
import builder.ValidationCommandBuilder

class CommandLineInterface(private val commandBuilders: MutableMap<String, CommandBuilder>) {
  private fun initializeCommandBuilders() {
    commandBuilders["validate"] = ValidationCommandBuilder()
  }

  // Formato de los commands:
  // validate helloWorld.ps 1.0
  // formatting helloWorld.ps 1.0 formatRules.yml
  fun execute(command: String): String {
    initializeCommandBuilders()

    val file = CommandParser.getFile(command)
    val operation = CommandParser.getOperation(command)
    val arguments = CommandParser.getArguments(command)

    val builder = commandBuilders[operation] ?: return "Unknown command: $command"

    val cmd = builder.build(file, arguments)

    val result = cmd.execute()

    return result
  }
}
