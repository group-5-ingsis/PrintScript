package utils

object CommandParser {
  fun getOperation(command: String): String {
    return command.split(" ")[0]
  }

  fun getFile(command: String): String {
    return command.split(" ")[1]
  }

  fun getVersion(command: String): String {
    return command.split(" ")[2]
  }

  fun getArguments(command: String): List<String> {
    return command.split(" ").drop(3)
  }
}
