package builder.command

import builder.CommandBuilder
import command.AnalyzeCommand
import command.Command

class AnalyzeCommandBuilder : CommandBuilder {
    override fun build(file: String, arguments: List<String>, version: String): Command {
        val fileWithRules = arguments.first()
        return AnalyzeCommand(file, version, fileWithRules)
    }
}
