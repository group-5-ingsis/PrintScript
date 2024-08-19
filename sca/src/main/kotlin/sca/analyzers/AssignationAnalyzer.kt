package sca.analyzers

import Node
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue

class AssignationAnalyzer : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: Node,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue> {
        return emptyList()
    }
}