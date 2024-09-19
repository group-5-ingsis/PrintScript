package cli

import kotlin.test.*

class CommandLineInterfaceTest {

    @Test
    fun `test correct File Validation`() {
        val command = "validate HelloWorld.ps 1.0"
        val result = CommandLineInterface.execute(command)
        assertEquals(result, "File Validated! (No Errors found)")
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
        assertEquals("File formatted successfully", result)
    }

    @Test
    fun `test failing File Validagion`() {
        val command = "validate WrongFile.ps 1.0"
        val result = CommandLineInterface.execute(command)
        assertEquals("Validation error: Invalid procedure: Type mismatch: Expected 'number' but found 'string' in variable 'hello'.", result)
    }

    @Test
    fun `test correct File Analisis`() {
        val command = "analyze HelloWorld.ps 1.0 linterRules.json"
        val result = CommandLineInterface.execute(command)
        assertEquals("No problems found", result)
    }
}
