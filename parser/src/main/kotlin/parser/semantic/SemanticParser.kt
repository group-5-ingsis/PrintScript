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
    fun validate(ast: StatementType, environment: Environment): Environment {
        val statementVisitor = StatementVisitor()
        val stringBuilder = StringBuilder()

        val visitorResult = ast.acceptVisitor(statementVisitor, environment, stringBuilder)
        val newEnvironment = visitorResult.second
        val result = runValidators(ast, newEnvironment)
        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return newEnvironment
        }
    }

    private fun runValidators(node: StatementType, environment: Environment): ValidationResult {
        return validator.validateSemantics(node, environment)
    }
}
