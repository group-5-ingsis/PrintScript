package cli

import position.Position
import kotlin.math.roundToInt

object ProgressTracker {
    private var progress: Int = 0
    private var totalProcessedCharacters: Int = 0

    fun calculateProcessedCharacters(
        fileContent: String,
        lastProcessedPosition: Position,
        endPosition: Position
    ): Int {
        val lines = fileContent.split("\n")

        var processedChars = 0

        if (lastProcessedPosition.line < lines.size) {
            val startLine = lines[lastProcessedPosition.line]
            processedChars += startLine.length - lastProcessedPosition.symbolIndex
        }

        for (lineIndex in (lastProcessedPosition.line + 1) until endPosition.line) {
            if (lineIndex < lines.size) {
                processedChars += lines[lineIndex].length
            }
        }

        if (endPosition.line < lines.size) {
            val endLine = lines[endPosition.line]
            processedChars += endPosition.symbolIndex
        }

        return processedChars
    }

    private fun countCharactersInSegment(
        startSymbolIndex: Int,
        endSymbolIndex: Int,
        segments: List<String>
    ): Int {
        var charCount = 0
        var currentIndex = 0

        for (segment in segments) {
            val segmentLength = segment.length
            if (currentIndex + segmentLength >= startSymbolIndex && currentIndex <= endSymbolIndex) {
                val start = if (currentIndex < startSymbolIndex) startSymbolIndex - currentIndex else 0
                val end = if (currentIndex + segmentLength > endSymbolIndex) endSymbolIndex - currentIndex else segmentLength
                charCount += end - start
            }
            currentIndex += segmentLength + 1
        }

        return charCount
    }

    fun updateProgress(processedCharacters: Int, totalCharacters: Int) {
        if (totalCharacters <= 0) throw IllegalArgumentException("Total characters must be greater than zero")
        totalProcessedCharacters += processedCharacters
        progress = calculateProgress(totalProcessedCharacters, totalCharacters)
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
