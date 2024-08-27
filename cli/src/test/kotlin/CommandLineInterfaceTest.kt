package cli

import kotlin.test.*

class CommandLineInterfaceTest {

    @Test
    fun `test correct File Validation`() {
        val command = "validate HelloWorld.ps 1.0"
        val result = CommandLineInterface.execute(command)
    }

    @Test
    fun `test correct File Execution`() {
        val command = "execute HelloWorld.ps 1.0"
        val result = CommandLineInterface.execute(command)
    }
}
