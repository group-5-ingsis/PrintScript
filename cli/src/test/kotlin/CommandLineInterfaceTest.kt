package cli

import kotlin.test.Test
import kotlin.test.assertEquals

class CommandLineInterfaceTest {
  @Test
  fun `dummyMethod should return formatted string`() {
    val result = CommandLineInterface.dummyMethod("test")
    assertEquals("Received: test", result)
  }
}
