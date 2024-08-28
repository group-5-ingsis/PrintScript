package parser

import Environment
import SemanticErrorException
import parser.validation.SemanticValidator
import parser.validation.ValidationResult
import visitor.NodeVisitor

class SemanticParser {
    private val validator = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun run(ast: SyntacticParser.RootNode): SyntacticParser.RootNode {
        val environment = Environment()
        ast.accept(NodeVisitor(environment))
        val result = runValidators(ast, environment)

        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return ast
        }
    }

    private fun runValidators(nodes: SyntacticParser.RootNode,environment: Environment ): ValidationResult {
        return validator.validateSemantics(nodes, environment)
    }


}
