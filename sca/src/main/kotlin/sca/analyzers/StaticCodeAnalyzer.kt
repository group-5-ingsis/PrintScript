package sca.analyzers

import Node
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue

interface StaticCodeAnalyzer {
    fun analyzeNode(
        astNode: Node,
        rules: StaticCodeAnalyzerRules,
    ): List<StaticCodeIssue>
}