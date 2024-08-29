package formatter

import java.io.File

object FileWriter {
    fun writeToFile(filePath: String, content: String) {
        val file = File(filePath)
        if (!file.exists()) {
            throw Exception("File not found: $filePath")
        }
        file.writeText(content)
    }
}
