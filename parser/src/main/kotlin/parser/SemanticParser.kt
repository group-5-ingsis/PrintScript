package parser

import composite.Node
import parser.exceptions.SemanticErrorException
import parser.validation.SemanticValidator
import parser.validation.ValidationResult

class SemanticParser {
    private val validator = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun run(ast: SyntacticParser.RootNode): SyntacticParser.RootNode {
        var tempTable = buildTempVariableTable(ast)
        val result = runValidators(ast.getChildren(), tempTable)
        tempTable = emptyList()
        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return ast
        }
    }

    private fun runValidators(nodes: List<Node>, variabletable: List<Node.Declaration>): ValidationResult {
        return validator.validateSemantics(nodes, variabletable)
    }

    private fun buildTempVariableTable(ast: SyntacticParser.RootNode): List<Node.Declaration> {
        val tempVariableTable = mutableListOf<Node.Declaration>()
        val nodes = ast.getChildren()
        for (node in nodes) {
            if (node is Node.Declaration) {
                tempVariableTable.add(node)
            }
            if (node is Node.AssignationDeclaration) {
                tempVariableTable.add(
                    Node.Declaration(node.dataType, node.kindVariableDeclaration, node.identifier)
                )
            }
        }
        return tempVariableTable
    }
}
