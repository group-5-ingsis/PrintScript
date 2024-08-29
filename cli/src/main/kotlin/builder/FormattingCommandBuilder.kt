package builder

import command.Command
import command.FormatCommand

class FormattingCommandBuilder : CommandBuilder {
    override fun build(file: String, arguments: List<String>, version: String): Command {
        val rulesFile = arguments.first()
        return FormatCommand(file, version, rulesFile)
    }
}
