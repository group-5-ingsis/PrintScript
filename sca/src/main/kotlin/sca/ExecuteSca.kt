package sca

import Node
import sca.analyzers.AssignationAnalyzer
import sca.analyzers.DeclarationAssignationAnalyzer
import sca.analyzers.MethodAnalyzer
import sca.analyzers.StaticCodeAnalyzer
import kotlin.reflect.KClass

class ExecuteSca(private val scaList: Map<KClass<out Node>, StaticCodeAnalyzer>) : StaticCodeAnalyzer {
    override fun analyzeNode(
        astNode: Node,
        rules: StaticCodeAnalyzerRules
    ): List<StaticCodeIssue> {
        val issues = mutableListOf<StaticCodeIssue>()
        val analyzer = scaList[astNode::class]

        if (analyzer != null) {
            analyzer.analyzeNode(astNode, rules).forEach {
                issues.add(it)
            }
        } else {
            throw IllegalArgumentException("No analyzer for this node")
        }

        return issues
    }

    companion object {

        fun getDefaultSCA(): ExecuteSca {
            return ExecuteSca(
                mapOf(
                    Node.AssignationDeclaration::class to DeclarationAssignationAnalyzer(),
                    Node.Declaration::class to AssignationAnalyzer(),
                    Node.Assignation::class to AssignationAnalyzer(),
                    Node.Method::class to MethodAnalyzer()
                )
            )
        }
    }
}
