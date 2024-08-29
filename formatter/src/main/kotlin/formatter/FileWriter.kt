package formatter

import java.io.File
import java.io.FileReader

object FileWriter {
    fun writeToFile(filePath: String, content: String) {
        val file = File(FileReader::class.java.classLoader.getResource(filePath)?.path ?: throw Exception("File not found"))
        file.writeText(content)
    }
}
