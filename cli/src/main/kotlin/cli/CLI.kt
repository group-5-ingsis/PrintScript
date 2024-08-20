package cli

import lexer.Lexer
import kotlin.jvm.Throws

object CLI {
    private val lexer: Lexer = Lexer

    fun run(argument: String) {
        val slicedParams: List<String> = argument.split(" ")
        try {
            validateArguments(slicedParams)
            // file: PSFile = ... (Logic for opening ps file and sending the string to the Lexer)
            // lexer.lex(file, slicedParams)
        } catch (e: IllegalArgumentException) {
            println("Caught exception ${e.message}")
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun validateArguments(slicedParams: List<String>) {
        if (slicedParams.size < 3) {
            throw IllegalArgumentException(
                "Invalid amount of arguments. Expected at least 3 arguments but got ${slicedParams.size}",
            )
        } else {
//            for ((index, arg) in slicedParams.withIndex()) {
//                // Validate parameter inputs.
//            }
        }
    }
}
