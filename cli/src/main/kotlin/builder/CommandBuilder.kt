package builder

import command.Command

interface CommandBuilder {
  fun build(
    file: String,
    arguments: List<String>,
  ): Command
}
