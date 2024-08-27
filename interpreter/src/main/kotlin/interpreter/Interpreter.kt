package interpreter

import parser.SyntacticParser
import parser.exceptions.SemanticErrorException
import validation.SemanticValidator
import validation.ValidationResult
import visitor.NodeVisitor
import kotlin.jvm.Throws

object Interpreter {
    private val validator = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun interpret(rootAstNode: SyntacticParser.RootNode): String {
        val result = runValidators(rootAstNode)
        if (result.isInvalid) {
            throw SemanticErrorException("Illegal procedure: " + result.message)
        } else {
            val nodeVisitor = NodeVisitor()
            rootAstNode.accept(nodeVisitor)
            val output = nodeVisitor.getOutput()
            return output
        }
    }

    private fun runValidators(rootAstNode: SyntacticParser.RootNode): ValidationResult {
        return validator.validateSemantics(rootAstNode)
    }
}
