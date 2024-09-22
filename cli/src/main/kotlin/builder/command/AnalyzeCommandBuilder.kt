package builder.command

import builder.CommandBuilder
import cli.utils.FileReader
import command.AnalyzeCommand
import command.Command

class AnalyzeCommandBuilder : CommandBuilder {
    override fun build(fileContent: String, arguments: List<String>, version: String): Command {
        val rulesLocation = arguments.first()
        val lintingRules = FileReader.getLinterRules(rulesLocation, version)
        return AnalyzeCommand(fileContent, version, lintingRules)
    }
}
