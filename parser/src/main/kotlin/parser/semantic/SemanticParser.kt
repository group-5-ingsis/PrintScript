package parser.semantic

import Environment
import exception.SemanticErrorException
import nodes.StatementType
import parser.semantic.validation.SemanticValidator
import parser.semantic.validation.ValidationResult
import position.visitor.StatementVisitor
import java.lang.StringBuilder

object SemanticParser {
    private val validator = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun validate(ast: StatementType): StatementType {
        val environment = Environment()
        val newEnv = ast.acceptVisitor(StatementVisitor(), environment, StringBuilder())
        val result = runValidators(ast, newEnv.second)

        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return ast
        }
    }

    private fun runValidators(node: StatementType, environment: Environment): ValidationResult {
        return validator.validateSemantics(node, environment)
    }
}
