package command

interface CommandBuilder {
  fun build(
    file: String,
    arguments: List<String>,
    version: String
  ): Command
}
