import java.io.File

object TxtConverter {
    fun readFileAsString(filePath: String): String {
        return File(filePath).readText(Charsets.UTF_8)
    }
}