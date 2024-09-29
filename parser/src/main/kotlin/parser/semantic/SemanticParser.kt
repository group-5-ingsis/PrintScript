package parser.semantic

import environment.Environment
import exception.SemanticErrorException
import nodes.Statement
import utils.InputProvider
import visitor.SemanticVisitor

object SemanticParser {

    fun validate(statement: Statement, environment: Environment, readInput: InputProvider): Statement {
        val semanticVisitor = SemanticVisitor(environment, readInput)
        val visitorResult = statement.accept(semanticVisitor)

        if (!visitorResult.isValid) {
            throw SemanticErrorException("Invalid procedure: " + visitorResult.message)
        }

        return statement
    }
}
