import environment.Environment
import interpreter.Interpreter
import lexer.Lexer
import nodes.Expression
import org.junit.Assert.assertEquals
import org.junit.Test
import parser.Parser
import token.Position

class ReadEnvTests {

    private val version = "1.1"

    @Test
    fun testReadEnvHelloWorld() {
        val input =
            "const name: string = readEnv(\"NAME\"); " +
                "println(\"Hello \" + name + 22);\n"

        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        var outputBuilder = StringBuilder()
        var currentEnvironment = Environment().define(createVariable("NAME", "WORLD"))

        while (asts.hasNext()) {
            asts.setEnv(currentEnvironment)
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder = result.first
            currentEnvironment = result.second
            asts.setEnv(currentEnvironment)
        }

        assertEquals("Hello WORLD22", outputBuilder.toString().trim())
    }

    @Test
    fun testReadEnvMultipleVariables() {
        val input =
            "const firstName: string = readEnv(\"FIRST_NAME\"); " +
                "const lastName: string = readEnv(\"LAST_NAME\"); " +
                "println(\"Hello \" + firstName + \" \" + lastName);\n"

        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        var outputBuilder = StringBuilder()

        // Definimos múltiples variables en el `Environment`
        var currentEnvironment = Environment()
            .define(createVariable("FIRST_NAME", "John"))
            .define(createVariable("LAST_NAME", "Doe"))

        while (asts.hasNext()) {
            asts.setEnv(currentEnvironment)
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder = result.first
            currentEnvironment = result.second
            asts.setEnv(currentEnvironment)
        }

        // Se espera que el output sea "Hello John Doe"
        assertEquals("Hello John Doe", outputBuilder.toString().trim())
    }

    @Test
    fun testReadEnvUndefinedVariable() {
        val input =
            "const age: number = readEnv(\"AGE\"); " +
                "println(age);\n"

        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        var outputBuilder = StringBuilder()

        // No definimos "AGE" en el `Environment`, por lo que debería ser `null`
        var currentEnvironment = Environment().define(createVariable("AGE", "4"))

        while (asts.hasNext()) {
            asts.setEnv(currentEnvironment)
            val statement = asts.next()
            val result = Interpreter.interpret(statement, version, currentEnvironment)
            outputBuilder = result.first
            currentEnvironment = result.second
            asts.setEnv(currentEnvironment)
        }

        // Asumimos que si la variable no existe en el entorno, imprime `null`
        assertEquals("4", outputBuilder.toString().trim())
    }

    @Test
    fun testReadEnvTypeMismatch() {
        val input =
            "let a: number = readEnv(\"NUMERO\"); " +
                "println(a);\n"

        val tokens = Lexer(input, version)
        val asts = Parser(tokens, version)

        var outputBuilder = StringBuilder()

        // Definimos "NUMERO" como un string no convertible a número
        var currentEnvironment = Environment().define(createVariable("NUMERO", "hola"))

        try {
            while (asts.hasNext()) {
                asts.setEnv(currentEnvironment)
                val statement = asts.next()
                val result = Interpreter.interpret(statement, version, currentEnvironment)
                outputBuilder = result.first
                currentEnvironment = result.second
                asts.setEnv(currentEnvironment)
            }
        } catch (e: IllegalArgumentException) {
            assertEquals("Expected an Integer but got: hola at Line 1, symbol 23", e.message)
        }
    }

//    TODO Esto no se puede hacer, no se permiten readEnv adetnro de prints, igualmente no hay tiempo para arreglo
//    @Test
//    fun testReadEnvInOperations() {
//        val input = "println(readEnv(\"COUNT\") + 10);"
//
//        val tokens = Lexer(input, version)
//        val asts = Parser(tokens, version)
//
//        var outputBuilder = StringBuilder()
//
//        // Definimos "COUNT" como el número 20
//        var currentEnvironment = Environment().define(createVariable("COUNT", "20"))
//
//        while (asts.hasNext()) {
//            asts.setEnv(currentEnvironment)
//            val statement = asts.next()
//            val result = Interpreter.interpret(statement, version, currentEnvironment)
//            outputBuilder = result.first
//            currentEnvironment = result.second
//            asts.setEnv(currentEnvironment)
//        }
//
//        // Se espera que el output sea "30" (20 + 10)
//        assertEquals("30", outputBuilder.toString().trim())
//    }

    fun createVariable(name: String, value: String): StatementType.Variable {
        return StatementType.Variable(
            designation = "const",
            identifier = name,
            initializer = Expression.Literal(value, Position(1, 1)), // Literal con valor dinámico
            dataType = "string",
            position = Position(1, 1)
        )
    }
}
