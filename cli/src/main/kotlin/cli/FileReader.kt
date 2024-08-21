package cli

import java.io.File

object FileReader {
  fun getFileContents(fileLocation: String): String {
    return try {
      val file = File(fileLocation)
      if (file.exists()) {
        file.readText()
      } else {
        "File not found."
      }
    } catch (e: Exception) {
      "Error reading file: ${e.message}"
    }
  }
}
