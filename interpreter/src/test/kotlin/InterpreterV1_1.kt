
import org.junit.Assert.*

class InterpreterV1_1 {

    val version = "1.1"

//    @Test
//    fun testDeclarationWithNumber() {
//        val input = "let a: boolean = true; if (a) { let queso: number = 5; println(queso); } else {println(\"hola\");}"
//        val version = "1.1"
//        val tokens = Lexer(input, version)
//        val asts = Parser(tokens, version)
//
//        val outputBuilder = StringBuilder()
//        var currentEnvironment = Environment()
//
//        while (asts.hasNext()) {
//            val statement = asts.next()
//            val result = Interpreter.interpret(statement, version)
//            outputBuilder.append(result.first.toString())
//            currentEnvironment = result.second
//        }
//
//        // Verificar el contenido del StringBuilder
//        val expectedOutput = "\n5\n" // Asumiendo que el valor de 'queso' se imprime seguido de un salto de línea
// //        assertEquals(expectedOutput, stringBuilder.toString())
// //        assertFalse(stringBuilder.toString().contains("hola"))
// //        assertTrue(env.contains("a"))
// //        assertFalse(env.contains("queso"))
//    }

//    @Test
//    fun testDeclarationReAssignation() {
//        val input = "let a: boolean = true; if (a) {a = false; let arbol : number = 44 ;  println(a); } else {println(\"hola\");}"
//        val version = "1.1"
//        val tokens = Lexer(input, version)
//        val asts = Parser(tokens, version)
//
//        // Ejecutar la interpretación
//        val outputBuilder = StringBuilder()
//        var currentEnvironment = Environment()
//
//        while (asts.hasNext()) {
//            val statement = asts.next()
//            val result = Interpreter.interpret(statement, version)
//            outputBuilder.append(result.first.toString())
//            currentEnvironment = result.second
//        }
//
//        // Verificar el contenido del StringBuilder
// //        val expectedOutput = "\nfalse\n" // Asumiendo que el valor de 'queso' se imprime seguido de un salto de línea
// //        assertEquals(expectedOutput, stringBuilder.toString())
// //
// //        assertTrue(env.contains("a"))
// //        assertEquals(env.get("a"), false)
// //        assertFalse(env.contains("arbol"))
//    }
}
