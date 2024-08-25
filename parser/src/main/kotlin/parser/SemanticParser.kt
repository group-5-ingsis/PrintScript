package parser

import parser.exceptions.SemanticErrorException
import parser.validators.SemanticValidator
import parser.validators.ValidationResult
import kotlin.jvm.Throws

class SemanticParser {
    private val validator = SemanticValidator()

    @Throws(SemanticErrorException::class)
    fun run(ast: SyntacticParser.RootNode): SyntacticParser.RootNode {
        val result: ValidationResult = validateAST(ast)
        if (result.isInvalid) {
            throw SemanticErrorException("Illegal procedure: " + result.message)
        } else {
            return ast
        }
    }

    private fun validateAST(ast: SyntacticParser.RootNode): ValidationResult {
        return validator.validateSemantics(ast)
    }
}
