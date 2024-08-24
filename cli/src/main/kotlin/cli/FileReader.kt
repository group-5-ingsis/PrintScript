package cli

import java.io.InputStream

object FileReader {
  fun getFileContents(
    file: String,
    version: String,
  ): String {
    val fileLocation = getFileLocation(file, version)
    return try {
      val inputStream: InputStream? = FileReader::class.java.classLoader.getResourceAsStream(fileLocation)
      inputStream?.bufferedReader()?.use { it.readText() } ?: "File not found."
    } catch (e: Exception) {
      "Error reading file: ${e.message}"
    }
  }

  private fun getFileLocation(
    file: String,
    version: String,
  ): String {
    // Path relative to the resources directory
    return "ps/$version/$file"
  }
}
