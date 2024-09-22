package command.analyze

import command.Command
import command.CommandBuilder
import utils.FileReader

class AnalyzeCommandBuilder : CommandBuilder {
    override fun build(file: String, arguments: List<String>, version: String): Command {
        val rulesLocation = arguments.first()
        val fileContent = FileReader.getFileContents(file, version)
        val lintingRules = FileReader.getLinterRules(rulesLocation, version)
        return AnalyzeCommand(fileContent, version, lintingRules)
    }
}
