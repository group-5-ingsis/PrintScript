package cli.utils

import lexer.Lexer

object ProgressTracker {

    fun updateProgress(lexer: Lexer, processedChars: Int, totalChars: Int): Int {
        val lexerProcessedChars = lexer.getProcessedCharacters()
        val progress = getProgress(processedChars, totalChars)
        println("Progress: $progress%")
        return lexerProcessedChars
    }

    private fun getProgress(processedChars: Int, totalChars: Int): Int {
        return if (totalChars > 0) {
            ((processedChars.toDouble() / totalChars) * 100).toInt()
        } else {
            0
        }
    }
}
