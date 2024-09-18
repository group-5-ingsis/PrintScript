package cli

import position.Position

object ProgressFetcher {

    fun calculateProcessedCharacters(
        fileContent: String,
        lastProcessedPosition: Position,
        endPosition: Position
    ): Int {
        val lines = fileContent.lines()
        var processedChars = 0

        val lastLine = lastProcessedPosition.line
        val endPositionLine = endPosition.line
        val lastSymbolIndex = lastProcessedPosition.symbolIndex
        val endPositionIndex = endPosition.symbolIndex

        if (lastLine == endPositionLine) {
            val line = lines[lastLine]
            if (lastSymbolIndex <= line.length && endPositionIndex <= line.length) {
                processedChars = line
                    .substring(lastSymbolIndex, endPositionIndex)
                    .count { !it.isWhitespace() }
            }
        } else {
            val lastLineContent = lines[lastLine]
            if (lastSymbolIndex <= lastLineContent.length) {
                processedChars += lastLineContent
                    .substring(lastSymbolIndex)
                    .count { !it.isWhitespace() }
            }

            for (lineIndex in (lastLine + 1) until endPositionLine) {
                processedChars += lines[lineIndex].count { !it.isWhitespace() }
            }

            val endLineContent = lines[endPositionLine]
            if (endPositionIndex <= endLineContent.length) {
                processedChars += endLineContent
                    .substring(0, endPositionIndex)
                    .count { !it.isWhitespace() }
            }
        }

        return processedChars
    }
}
