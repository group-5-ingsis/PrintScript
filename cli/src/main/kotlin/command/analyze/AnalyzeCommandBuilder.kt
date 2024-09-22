package command.analyze

import command.Command
import command.CommandBuilder
import utils.FileReader

class AnalyzeCommandBuilder : CommandBuilder {
    override fun build(file: String, arguments: List<String>, version: String): Command {
        val rulesFile = arguments.first()
        val rules = FileReader.getFileContents(rulesFile, version)
        val fileContent = FileReader.getFileContents(file, version)
        val lintingRules = FileReader.getLinterRules(rules, version)
        return AnalyzeCommand(fileContent, version, lintingRules)
    }
}
