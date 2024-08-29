package cli

import java.io.File
import java.io.OutputStream

object FileWriter {
    fun writeToFile(fileName: String, version: String, content: String) {
        val fileLocation = FileReader.getFileLocation(fileName, version)
        val file = File(fileLocation)

        try {
            file.parentFile.mkdirs()

            file.outputStream().use { outputStream: OutputStream ->
                outputStream.write(content.toByteArray())
            }
        } catch (e: Exception) {
            throw Exception("Error writing to file: ${e.message}")
        }
    }
}
