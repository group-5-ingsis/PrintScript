package linter

import nodes.StatementType
import rules.LinterRules

class Linter(private val rules: LinterRules) {

    private val errors = mutableListOf<LinterResult>()
    private val allResults = mutableListOf<LinterResult>()

    fun lint(parser: Iterator<StatementType>): LinterResult {
        while (parser.hasNext()) {
            val linterVisitor = LinterVisitor(rules)
            val astNode = parser.next()
            astNode.accept(linterVisitor)
            val linterResult = linterVisitor.getLinterResult()
            allResults.add(linterResult)
            if (!linterResult.isValid()) {
                errors.add(linterResult)
            }
        }
        return if (errors.isEmpty()) {
            LinterResult(true, "No errors found")
        } else {
            LinterResult(false, "Errors found, see getErrors() for details")
        }
    }

    fun getErrors(): List<LinterResult> {
        return errors
    }

    fun getAllResults(): List<LinterResult> {
        return allResults
    }
}
