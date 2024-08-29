package cli

import kotlin.test.*

class CommandLineInterfaceTest {

    @Test
    fun `test correct File Validation`() {
        val command = "validate HelloWorld.ps 1.0"
        val result = CommandLineInterface.execute(command)
        assertEquals(result, "File Validated")
    }

    @Test
    fun `test correct File Execution`() {
        val command = "execute HelloWorld.ps 1.0"
        val result = CommandLineInterface.execute(command)
        assertEquals(result, "\nFinished executing HelloWorld.ps")
    }

    @Test
    fun `test correct File Formatting`() {
        val command = "format HelloWorld.ps 1.0 rules.yaml"
        val result = CommandLineInterface.execute(command)
        assertEquals(result, "File formatted successfully")
    }

    @Test
    fun `test failing File Validagion`() {
        val command = "validate WrongFile.ps 1.0"
        val result = CommandLineInterface.execute(command)
        assertEquals("Validation error: Invalid procedure: Declared type NUMBER does not match assigned type STRING", result)
    }

    @Test
    fun `test correct File Analisis`() {
        val command = "analyze HelloWorld.ps 1.0 rules.yaml"
        val result = CommandLineInterface.execute(command)
        assertEquals("Need More Coverage", result)
    }
}
