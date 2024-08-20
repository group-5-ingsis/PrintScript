package parser

import parser.exceptions.SemanticErrorException
import parser.validators.ValidationResult
import kotlin.jvm.Throws

class SemanticParser {
    @Throws(SemanticErrorException::class)
    fun run(ast: SyntacticParser.RootNode): SyntacticParser.RootNode {
        val result: ValidationResult = validateAST(ast)
        if (result.isInvalid) {
            throw SemanticErrorException("Illegal procedure: " + result.message)
        } else {
            return ast
        }
    }

    // TODO replace List<Node> for SemanticParsingResult object.
    @Throws(SemanticErrorException::class)
    private fun validateAST(ast: SyntacticParser.RootNode): ValidationResult {
        // Logic for validating semantically
        return ValidationResult(false, null, "")
    }
}
