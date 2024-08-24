package cli

import kotlin.test.Test
import kotlin.test.assertTrue

class CommandLineInterfaceTest {
  @Test
  fun `test if correct builder is grabbed for validate command`() {
    val command = "validate HelloWorld.ps 1.0"
    val result = CommandLineInterface.execute(command)

    assertTrue(result.isNotEmpty(), "Command should return a non-empty result")
  }
}
