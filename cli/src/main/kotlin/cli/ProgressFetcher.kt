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
            processedChars = endPositionIndex - lastSymbolIndex
        } else {
            processedChars += lines[lastLine].length - lastSymbolIndex

            for (lineIndex in (lastLine + 1) until endPositionLine) {
                processedChars += lines[lineIndex].length
            }

            processedChars += endPositionIndex
        }

        return processedChars
    }
}
