package command.format

import command.Command
import command.CommandBuilder
import utils.FileReader

class FormattingCommandBuilder : CommandBuilder {
    override fun build(fileContent: String, arguments: List<String>, version: String): Command {
        val rulesFile = arguments.first()
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)
        return FormatCommand(fileContent, version, formattingRules)
    }
}
