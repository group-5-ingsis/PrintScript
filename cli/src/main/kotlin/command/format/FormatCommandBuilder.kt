package command.format

import command.Command
import command.CommandBuilder
import utils.FileReader

class FormatCommandBuilder : CommandBuilder {
    override fun build(file: String, arguments: List<String>, version: String): Command {
        val rulesFile = arguments.first()
        val formattingRules = FileReader.getFormattingRules(rulesFile, version)
        return FormatCommand(file, version, formattingRules)
    }
}
