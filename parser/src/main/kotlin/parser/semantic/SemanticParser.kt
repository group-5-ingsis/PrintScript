package parser.semantic

import environment.Environment
import exception.SemanticErrorException
import nodes.Statement
import parser.semantic.validation.SemanticValidator
import parser.semantic.validation.ValidationResult
import visitor.StatementVisitor
import java.lang.StringBuilder

object SemanticParser {

    @Throws(SemanticErrorException::class)
    fun validate(ast: Statement, environment: Environment, readInput: String?): Environment {
        val statementVisitor = StatementVisitor(readInput)
        val stringBuilder = StringBuilder()

        val visitorResult = ast.acceptVisitor(statementVisitor, environment, stringBuilder)
        val newEnvironment = visitorResult.second
        val result = runValidators(ast, newEnvironment, readInput)
        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return newEnvironment
        }
    }

    private fun runValidators(node: Statement, environment: Environment, readInput: String? = null): ValidationResult {
        val validator = SemanticValidator(readInput)
        return validator.validateSemantics(node, environment)
    }
}
