package parser.validation

import Environment
import nodes.StatementType

class PrintValidator : Validator<StatementType.Print> {

    override fun validate(node: StatementType.Print, scope: Environment): ValidationResult {
        TODO("Not yet implemented")
    }
}
