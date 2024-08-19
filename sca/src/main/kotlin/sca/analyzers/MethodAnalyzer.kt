package sca.analyzers

import Node
import Position
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue

class MethodAnalyzer : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: Node,
        rules: StaticCodeAnalyzerRules
    ): List<StaticCodeIssue> {
        val methodNode = astNode as Node.Method
        val issues = mutableListOf<StaticCodeIssue>()
        val lineIndex = 1
        var columnIndex = 1

        if (methodNode.identifier.value == "println") {
            val argument = extractArgument(methodNode.arguments)
            if (!checkMethodArgument(argument, rules.functionArgumentCheck)) {
                columnIndex++
                issues.add(StaticCodeIssue("The println function should be called only with an identifier or a literal, the expression: $argument is invalid.", Position(lineIndex, columnIndex)))
            }
        }
        return issues
    }

    private fun extractArgument(value: Node.Arguments): String {
        var result = ""
        for (arguments in value.argumentsOfAnyTypes) {
            when (arguments) {
                is Node.GenericLiteral -> result += arguments.value
                is Node.Identifier -> result += arguments.value
                else -> ""
            }
        }
        return result
    }

    private fun checkMethodArgument(
        argument: String,
        rule: Boolean
    ): Boolean {
        // Verificar si el argumento es un identificador o un literal (número o string)
        if (!rule) return true // Si las reglas están desactivadas, siempre retorna true
        return argument.matches("""^[\w\d]+$""".toRegex())
    }
}
