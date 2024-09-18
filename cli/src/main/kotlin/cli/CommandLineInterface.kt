package cli

import builder.*

object CommandLineInterface {
    private val commandBuilders: Map<String, CommandBuilder> = initializeCommandBuilders()

    private fun initializeCommandBuilders(): Map<String, CommandBuilder> {
        return mapOf(
//            "validate" to ValidationCommandBuilder(),
//            "execute" to ExecuteCommandBuilder(),
            "format" to FormattingCommandBuilder(),
            "analyze" to AnalyzeCommandBuilder()
        )
    }

    fun execute(command: String): String {
        val file = CommandParser.getFile(command)
        val operation = CommandParser.getOperation(command)
        val version = CommandParser.getVersion(command)
        val arguments = CommandParser.getArguments(command)

        val builder = commandBuilders[operation] ?: return "Unknown command: $command"

        val cmd = builder.build(file, arguments, version)
        val result = cmd.execute()

        return result
    }
}
