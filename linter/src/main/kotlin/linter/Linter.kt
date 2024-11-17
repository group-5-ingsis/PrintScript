package linter

import nodes.Statement
import rules.LinterRules

object Linter {

  fun lint(statement: Statement, rules: LinterRules, version: String): LinterResult {
    val errors = mutableListOf<LinterResult>()
    val linterVisitor = LinterVisitor(rules)

    statement.accept(linterVisitor)
    val linterResult = linterVisitor.getLinterResult()

    if (!linterResult.isValid()) {
      errors.add(linterResult)
    }

    val validStatement = errors.isEmpty()

    return if (validStatement) {
      LinterResult(true, "No errors found")
    } else {
      val errorMessages = errors.joinToString("\n") { it.message }
      LinterResult(false, "Errors found:\n$errorMessages")
    }
  }
}
