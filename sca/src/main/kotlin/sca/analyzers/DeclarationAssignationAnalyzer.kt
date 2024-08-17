package sca.analyzers

import Node
import sca.StaticCodeIssue
import Position
import sca.StaticCodeAnalyzerRules

class DeclarationAssignationAnalyzer: StaticCodeAnalyzer {
    override fun analyzeNode(astNode: Node, rules: StaticCodeAnalyzerRules): List<StaticCodeIssue> {
        val declarationAssignation = astNode as Node.AssignationDeclaration
        val issues = mutableListOf<StaticCodeIssue>()
        val lineIndex = 1
        var columnIndex = 1
        if (!checkTypeMatching(declarationAssignation, rules.typeMatchingCheck)) {
            columnIndex += 3
            issues.add(StaticCodeIssue("Variable declaration does not match the type of the assigned value", Position(lineIndex, columnIndex)))
        }
        if (!checkIdentifierFormat(declarationAssignation.identifier, rules.identifierNamingCheck)) {
            columnIndex += 1
            issues.add(StaticCodeIssue("The identifier '${declarationAssignation.identifier}' must be in lower camel case.", Position(lineIndex, columnIndex)))
        }
        return issues
    }

    private fun checkTypeMatching(
        node: Node,
        rule: Boolean,
    ): Boolean {
        if (!rule) return true
        return when (node) {
            is Node.AssignationDeclaration -> {
                when (node.dataType.type) {
                    "STRING" -> (node.value as Node.GenericLiteral).dataType.type == "STRING"
                    "NUMBER" -> (node.value as Node.GenericLiteral).dataType.type == "NUMBER"
                    else -> false
                }
            }
            else -> false
        }
    }

    private fun checkIdentifierFormat(
        identifier: String,
        rule: Boolean,
    ): Boolean {
        if (!rule) return true
        return identifier.matches("""^[a-z]+(?:[A-Z][a-z\d]*)*$""".toRegex())
    }
}