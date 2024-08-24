package cli

import builder.CommandBuilder
import builder.ValidationCommandBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommandLineInterfaceTest {
  @Test
  fun `test if correct builder is grabbed for validate command`() {
    val validationCommandBuilder = ValidationCommandBuilder()
    val commandBuilders = mutableMapOf<String, CommandBuilder>()
    commandBuilders["validate"] = validationCommandBuilder

    val command = "validate helloWorld.ps 1.0"
    val result = CommandLineInterface.execute(command)

    assertNotNull(commandBuilders["validate"], "ValidationCommandBuilder should be present")
    assertTrue(result.isNotEmpty(), "Command should return a non-empty result")
  }
}
