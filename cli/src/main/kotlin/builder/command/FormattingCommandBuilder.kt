package builder.command

import builder.CommandBuilder
import cli.utils.FileReader
import command.Command
import command.FormatCommand

class FormattingCommandBuilder : CommandBuilder {
    override fun build(fileContent: String, arguments: List<String>, version: String): Command {
        val rulesFile = arguments.first()
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)
        return FormatCommand(fileContent, version, formattingRules)
    }
}
