package cli

import java.io.File

object FileReader {
  fun getFileContents(
    file: String,
    version: String,
  ): String {
    val fileLocation = getFileLocation(file, version)
    return try {
      val fileName = File(fileLocation)
      if (fileName.exists()) {
        fileName.readText()
      } else {
        "File not found."
      }
    } catch (e: Exception) {
      "Error reading file: ${e.message}"
    }
  }

  private fun getFileLocation(
    file: String,
    version: String,
  ): String {
    val fileLocation = "${System.getProperty("user.dir")}/../ps/$version/$file"
    return fileLocation
  }
}
