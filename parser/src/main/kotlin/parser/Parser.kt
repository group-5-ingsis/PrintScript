package parser

import Environment
import SemanticErrorException
import parser.validation.SemanticValidator
import token.Token
import visitor.NodeVisitor


// Singleton object since it can be reused with different arguments.
class Parser {
    private val syntacticParser = SemanticValidator()




    @Throws(SemanticErrorException::class)
    fun run(tokens: List<Token>): SyntacticParser.RootNode {

        val ast = SyntacticParser(tokens).parse()
        val environment = Environment()
        ast.accept(NodeVisitor(environment))
        val validation = syntacticParser.validateSemantics(ast, environment)
        println("The validation was: " + validation.isInvalid)
        return ast


    }




}
