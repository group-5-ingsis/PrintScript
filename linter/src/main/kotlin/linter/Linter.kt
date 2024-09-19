package linter

import nodes.StatementType
import rules.LinterRules

object Linter {
    private val errors = mutableListOf<LinterResult>()
    private val allResults = mutableListOf<LinterResult>()

    fun lint(statement: StatementType, rules: LinterRules): LinterResult {
        val linterVisitor = LinterVisitor(rules)

        statement.accept(linterVisitor)
        val linterResult = linterVisitor.getLinterResult()

        allResults.add(linterResult)

        if (!linterResult.isValid()) {
            errors.add(linterResult)
        }

        return if (errors.isEmpty()) {
            LinterResult(true, "No errors found")
        } else {
            LinterResult(false, "Errors found, see getErrors() for details")
        }
    }

    fun getErrors(): List<LinterResult> = errors

    fun clearResults() {
        errors.clear()
        allResults.clear()
    }
}
