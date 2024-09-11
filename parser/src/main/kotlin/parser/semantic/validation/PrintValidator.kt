package parser.semantic.validation

import nodes.StatementType
import position.visitor.Environment

class PrintValidator : Validator<StatementType.Print> {

    override fun validate(node: StatementType.Print, scope: Environment): ValidationResult {
        TODO("Not yet implemented")
    }
}
