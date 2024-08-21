package builder

import command.Command

interface CommandBuilder {
  fun build(arguments: String): Command
}
