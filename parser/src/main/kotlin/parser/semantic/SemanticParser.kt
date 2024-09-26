package parser.semantic

import environment.Environment
import exception.SemanticErrorException
import nodes.Statement
import parser.semantic.validation.SemanticValidator
import parser.semantic.validation.ValidationResult
import visitor.InputProvider
import visitor.InterpreterVisitor

object SemanticParser {

    @Throws(SemanticErrorException::class)
    fun validate(ast: Statement, environment: Environment, readInput: InputProvider): Environment {
        val statementVisitor = InterpreterVisitor(environment)
        val visitorResult = ast.accept(statementVisitor)
        val newEnvironment = visitorResult.second
        val result = runValidators(ast, newEnvironment, readInput)
        if (result.isInvalid) {
            throw SemanticErrorException("Invalid procedure: " + result.message)
        } else {
            return newEnvironment
        }
    }

    private fun runValidators(node: Statement, environment: Environment, inputProvider: InputProvider): ValidationResult {
        val validator = SemanticValidator(inputProvider)
        return validator.validateSemantics(node, environment)
    }
}
