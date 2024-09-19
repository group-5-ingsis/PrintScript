package cli

import position.Position
import kotlin.math.roundToInt

object ProgressTracker {
    private var progress: Int = 0

    fun calculateProcessedCharacters(
        fileContent: String,
        lastProcessedPosition: Position,
        endPosition: Position
    ): Int {
        val lines = fileContent.lines()
        val lastLineIndex = lastProcessedPosition.line
        val endLineIndex = endPosition.line

        var processedChars = 0

        if (lastLineIndex == endLineIndex) {
            processedChars = countCharactersInWords(
                lastProcessedPosition.symbolIndex,
                endPosition.symbolIndex,
                lines[lastLineIndex]
            )
        } else {
            processedChars += countCharactersInWords(
                lastProcessedPosition.symbolIndex,
                Int.MAX_VALUE,
                lines[lastLineIndex]
            )
        }

        return processedChars
    }

    private fun countCharactersInWords(
        startSymbolIndex: Int,
        endSymbolIndex: Int,
        line: String
    ): Int {
        val words = line.split("\\s+".toRegex())
        var charCount = 0

        var startWordIndex = startSymbolIndex
        var endWordIndex = endSymbolIndex

        if (startWordIndex > 0) startWordIndex--

        for (i in startWordIndex.coerceAtMost(words.size - 1)..endWordIndex.coerceAtMost(words.size - 1)) {
            charCount += words[i].length
            if (i < endWordIndex && i < words.size - 1) charCount += 1
        }

        return charCount
    }

    fun updateProgress(processedCharacters: Int, totalCharacters: Int) {
        if (totalCharacters <= 0) throw IllegalArgumentException("Total characters must be greater than zero")

        progress = calculateProgress(processedCharacters, totalCharacters)
        reportProgress()
    }

    private fun calculateProgress(processedCharacters: Int, totalCharacters: Int): Int {
        return (processedCharacters.toDouble() / totalCharacters * 100).roundToInt()
    }

    private fun reportProgress() {
        println("Progress: $progress%")
    }

    fun getProgress(): Int {
        return progress
    }
}
