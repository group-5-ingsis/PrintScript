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

//    @Test
//    fun `test correct File Analisis`() {
//        val command = "analyze HelloWorld.ps 1.0 rules.yaml"
//        val result = CommandLineInterface.execute(command)
//        assertEquals(result,"File with no problems")
//    }
}
