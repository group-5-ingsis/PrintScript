package cli

import java.io.File

object FileReader {
  fun getFileContents(fileLocation: String): String {
    return try {
      val file = File(fileLocation)
      val fileExists = file.exists()
      if (fileExists) {
        file.readText()
      } else {
        "File not found."
      }
    } catch (e: Exception) {
      "Error reading file: ${e.message}"
    }
  }
}
