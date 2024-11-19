package parser

import environment.EnvironmentCreator
import exception.AllowedException
import nodes.Statement
import parser.semantic.SemanticParser
import parser.syntactic.SyntacticParser
import token.Token
import utils.InputProvider

class Parser(private val tokenIterator: Iterator<Token>, private val version: String = "1.1") : Iterator<Statement> {

  private var readInput: InputProvider = InputProvider()
  private val environment = EnvironmentCreator.create(System.getenv())

  override fun hasNext(): Boolean {
    return tokenIterator.hasNext()
  }

  override fun next(): Statement {
    var tokens: List<Token> = listOf()
    var lastException: Exception? = null

    while (tokenIterator.hasNext()) {
      val nextToken = tokenIterator.next()
      tokens = tokens + nextToken

      try {
        var statement = SyntacticParser.parse(tokens, version)

        statement = SemanticParser.validate(statement, environment, readInput)

        return statement
      } catch (e: Exception) {
        if (!tokenIterator.hasNext()) {
          throw e
        }
        if (e !is AllowedException) {
          if (lastException != null && lastException::class == e::class && lastException.message == e.message) {
            throw e
          }
        }

        lastException = e
      }
    }

    throw NoSuchElementException("No more tokens available to parse")
  }
}
