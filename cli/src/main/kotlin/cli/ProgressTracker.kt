package cli

object ProgressTracker {

    fun updateProgress(processedChars: Int, totalChars: Int) {
        if (totalChars > 0) {
            val progress = (processedChars.toDouble() / totalChars) * 100
            println("Progress: ${progress.toInt()}% ($processedChars / $totalChars characters)")
        }
    }
}
