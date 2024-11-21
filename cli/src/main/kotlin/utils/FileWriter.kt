package utils

import java.io.File
import java.io.OutputStream

object FileWriter {
  fun writeToFile(fileName: String, version: String, content: String): File {
    val fileLocation = getFileLocation(fileName, version)
    val file = File(fileLocation)

    runCatching {
      file.parentFile.mkdirs()

      file.outputStream().use { outputStream: OutputStream ->
        outputStream.write(content.toByteArray())
      }
    }.onFailure {
      throw Exception("Error writing to file: ${it.message}")
    }

    return file
  }

  private fun getFileLocation(file: String, version: String): String {
    val baseDirectory = "src/main/resources/ps/$version/"
    return "$baseDirectory$file"
  }
}
