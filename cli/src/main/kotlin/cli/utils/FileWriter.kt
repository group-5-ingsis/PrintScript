package cli.utils

import java.io.File
import java.io.OutputStream

object FileWriter {
    fun writeToFile(fileName: String, version: String, content: String) {
        val fileLocation = getFileLocation(fileName, version)
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

    private fun getFileLocation(file: String, version: String): String {
        val baseDirectory = "src/main/resources/ps/$version/"
        return "$baseDirectory$file"
    }
}
