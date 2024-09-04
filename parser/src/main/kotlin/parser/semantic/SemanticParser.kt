package parser.semantic

import Environment
import exception.SemanticErrorException
import parser.semantic.validation.SemanticValidator
import parser.semantic.validation.ValidationResult
import parser.syntactic.SyntacticParser
import position.visitor.StatementVisitor

class SemanticParser {
    private val validator = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun run(ast: SyntacticParser.RootNode): SyntacticParser.RootNode {
        val environment = Environment()
        val newEnv = ast.accept(StatementVisitor(), environment)
        val result = runValidators(ast, newEnv)

        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return ast
        }
    }

    private fun runValidators(nodes: SyntacticParser.RootNode, environment: Environment): ValidationResult {
        return validator.validateSemantics(nodes, environment)
    }
}
