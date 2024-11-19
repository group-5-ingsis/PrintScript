// package parser
//
// import exception.SemanticErrorException
// import lexer.Lexer
// import org.junit.Test
// import kotlin.test.assertFailsWith
//
// class SemanticTesting {
//
//  @Test
//  fun testConstDeclarationShouldFail() {
//    /* Declaration of const. */
//    val lexer = Lexer("const a: string;")
//    val parser = Parser(lexer)
//    assertFailsWith(SemanticErrorException::class) {
//      parser.next()
//    }
//  }
//
//  @Test
//  fun testAssignationShouldPass() {
//    val lexer = Lexer("let a: string; a = \"testing\";")
//    val parser = Parser(lexer)
//    println(parser.next())
//  }
//
//  @Test
//  fun testAssignationToDifferentTypeShouldFail() {
//    val lexer = Lexer("let a: string; a = 22;")
//    val parser = Parser(lexer)
//    assertFailsWith(SemanticErrorException::class) {
//      parser.next()
//      parser.next()
//    }
//  }
//
//  @Test
//  fun testAssignationOfConstShouldFail() {
//    val lexer = Lexer("const a: string = \"test\"; a = \"testing\";")
//    val parser = Parser(lexer)
//    assertFailsWith(Exception::class) {
//      parser.next()
//      parser.next()
//    }
//  }
//
//  @Test
//  fun testAssignationToInexistentVariableShouldFail() {
//    val lexer = Lexer("let a: string; a = b;")
//    val parser = Parser(lexer)
//    assertFailsWith(Error::class) {
//      parser.next()
//      parser.next()
//    }
//  }
//
//  @Test
//  fun testAssignationOfInexistentVariableShouldFail() {
//    val lexer = Lexer("b = 23;")
//    val parser = Parser(lexer)
//    assertFailsWith(Error::class) {
//      parser.next()
//    }
//  }
//
//  @Test
//  fun testAssignDeclareShouldPass() {
//    val lexer = Lexer("let b: number = 23; let b: string;")
//    val parser = Parser(lexer)
//    println(parser.next())
//  }
//
//  @Test
//  fun testAssignDeclareShouldFail() {
//    val lexer = Lexer("const b: string = 23;")
//    val parser = Parser(lexer)
//    assertFailsWith(SemanticErrorException::class) {
//      parser.next()
//    }
//  }
//
//  @Test
//  fun testPrintlnCorrect() {
//    val lexer = Lexer("let b: string = 23; println(b)")
//    val parser = Parser(lexer)
//  }
//
//  @Test
//  fun testPrintlnMultipleArguments() {
//    val lexer = Lexer("let b: string = 23; println(b, 2)")
//    val parser = Parser(lexer)
//  }
//
//  @Test
//  fun testPrintlnUnknownVariable() {
//    val tokens = Lexer("let something: string; println(something);")
//    val parser = Parser(tokens)
//    assertFailsWith(SemanticErrorException::class) {
//      val next = parser.next()
//      val next2 = parser.next()
//    }
//  }
//
//  @Test
//  fun testPrintVariable() {
//    val tokens = Lexer("let something: number = 23; println(something);")
//    val parser = Parser(tokens)
//    val next = parser.next()
//    val next2 = parser.next()
//  }
//
//  @Test
//  fun testPrintNumber() {
//    val tokens = Lexer("println(1);")
//    val parser = Parser(tokens)
//    parser.next()
//  }
//
//  @Test
//  fun testPrintString() {
//    val tokens = Lexer("println(\"Hello World!\");")
//    val parser = Parser(tokens)
//    parser.next()
//  }
//
//  @Test
//  fun testPrintSum() {
//    val tokens = Lexer("println(3 + 4);")
//    val parser = Parser(tokens)
//    parser.next()
//  }
// }
