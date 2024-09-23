package parser.semantic

import environment.Environment
import exception.SemanticErrorException
import nodes.StatementType
import parser.semantic.validation.SemanticValidator
import parser.semantic.validation.ValidationResult
import position.visitor.InputProvider
import visitor.StatementVisitor
import java.lang.StringBuilder

object SemanticParser {

    @Throws(SemanticErrorException::class)
    fun validate(ast: StatementType, environment: Environment, readInput: InputProvider): Environment {
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

    private fun runValidators(node: StatementType, environment: Environment, inputProvider: InputProvider): ValidationResult {
        val validator = SemanticValidator(inputProvider)
        return validator.validateSemantics(node, environment)
    }
}
