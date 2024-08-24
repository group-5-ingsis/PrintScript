package cli

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandLineInterfaceTest {
  @Test
  fun `test correct File Validation`() {
    val command = "validate HelloWorld.ps 1.0"
    val result = CommandLineInterface.execute(command)

    assertEquals("File Validated", result)
  }
}
