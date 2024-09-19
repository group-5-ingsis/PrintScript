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
        val lines = fileContent.split("\n")
        val lastLineIndex = lastProcessedPosition.line
        var processedChars = 0

        val startSymbolIndex = lastProcessedPosition.symbolIndex
        val endSymbolIndex = endPosition.symbolIndex
        val line = lines[lastLineIndex]
        processedChars += countCharactersInSegment(
            startSymbolIndex,
            endSymbolIndex,
            line
        )

        return processedChars
    }

    private fun countCharactersInSegment(
        startSymbolIndex: Int,
        endSymbolIndex: Int,
        line: String
    ): Int {
        val segments = line.split(";")
        var charCount = 0

        val startSegmentIndex = findSegmentIndex(startSymbolIndex, segments)
        val endSegmentIndex = findSegmentIndex(endSymbolIndex, segments)

        if (startSegmentIndex == -1 || endSegmentIndex == -1) return 0

        if (startSegmentIndex == endSegmentIndex) {
            charCount += segments[startSegmentIndex].substring(startSymbolIndex).length
        } else {
            charCount += segments[startSegmentIndex].substring(startSymbolIndex).length
            charCount += segments[endSegmentIndex].substring(0, endSymbolIndex).length

            for (i in (startSegmentIndex + 1) until endSegmentIndex) {
                charCount += segments[i].length
            }
        }

        return charCount
    }

    private fun findSegmentIndex(symbolIndex: Int, segments: List<String>): Int {
        var index = 0
        var count = 0

        while (index < segments.size) {
            count += segments[index].length + 1 // Include space for the semicolon
            if (count > symbolIndex) return index
            index++
        }

        return -1
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
