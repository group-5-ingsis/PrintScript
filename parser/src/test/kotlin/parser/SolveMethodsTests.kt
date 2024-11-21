package parser

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertThrows
import visitor.BinaryOperator.convertToDouble
import visitor.BinaryOperator.solveNumberAndNumber
import visitor.BinaryOperator.solveNumberAndString
import visitor.BinaryOperator.solveStringAndNumber
import kotlin.test.Test

class SolveMethodsTests {

  @Test
  fun `solveStringAndNumber should concatenate String and Number with plus operator`() {
    assertEquals("hello5", solveStringAndNumber("\"hello\"", 5, "+"))
    assertEquals("text3.14", solveStringAndNumber("\"text\"", 3.14, "+"))
  }

  @Test
  fun `solveStringAndNumber should throw exception for unsupported operators`() {
    val exception = assertThrows(IllegalArgumentException::class.java) {
      solveStringAndNumber("\"hello\"", 5, "-")
    }
    assertTrue(exception.message!!.contains("Unsupported operation between String and Number"))
  }

  @Test
  fun `solveNumberAndString should concatenate Number and String with plus operator`() {
    assertEquals("5hello", solveNumberAndString(5, "\"hello\"", "+"))
    assertEquals("3.14text", solveNumberAndString(3.14, "\"text\"", "+"))
  }

  @Test
  fun `solveNumberAndString should throw exception for unsupported operators`() {
    val exception = assertThrows(IllegalArgumentException::class.java) {
      solveNumberAndString(5, "\"hello\"", "*")
    }
    assertTrue(exception.message!!.contains("Unsupported operation between Number and String"))
  }

  @Test
  fun `solveNumberAndNumber should perform arithmetic operations for integers`() {
    assertEquals(10, solveNumberAndNumber(5, 5, "+"))
    assertEquals(0, solveNumberAndNumber(5, 5, "-"))
    assertEquals(25, solveNumberAndNumber(5, 5, "*"))
    assertEquals(1, solveNumberAndNumber(5, 5, "/"))
  }

  @Test
  fun `solveNumberAndNumber should perform arithmetic operations for doubles`() {
    assertEquals(10.0, solveNumberAndNumber(5.0, 5.0, "+"))
    assertEquals(0.0, solveNumberAndNumber(5.0, 5.0, "-"))
    assertEquals(25.0, solveNumberAndNumber(5.0, 5.0, "*"))
    assertEquals(1.0, solveNumberAndNumber(5.0, 5.0, "/"))
  }

  @Test
  fun `solveNumberAndNumber should perform operations between int and double`() {
    assertEquals(10.5, solveNumberAndNumber(5, 5.5, "+"))
    assertEquals(5.5, solveNumberAndNumber(11, 5.5, "-"))
  }

  @Test
  fun `solveNumberAndNumber should throw exception for unsupported operators`() {
    val exception = assertThrows(IllegalArgumentException::class.java) {
      solveNumberAndNumber(5, 5, "%")
    }
    assertTrue(exception.message!!.contains("Unsupported number operation"))
  }

  @Test
  fun `convertToDouble should convert various numeric types to Double`() {
    assertEquals(5.0, convertToDouble(5))
    assertEquals(5.0, convertToDouble(5L))
    assertEquals(5.5, convertToDouble(5.5))
    assertEquals(5.5, convertToDouble(5.5f))
  }

  @Test
  fun `convertToDouble should convert valid String to Double`() {
    assertEquals(3.14, convertToDouble("3.14"))
    assertEquals(42.0, convertToDouble("42"))
  }

  @Test
  fun `convertToDouble should throw exception for invalid String`() {
    val exception = assertThrows(IllegalArgumentException::class.java) {
      convertToDouble("notANumber")
    }
    assertTrue(exception.message!!.contains("Cannot convert String to Double"))
  }

  @Test
  fun `convertToDouble should throw exception for unsupported types`() {
    val exception = assertThrows(IllegalArgumentException::class.java) {
      convertToDouble(true)
    }
    assertTrue(exception.message!!.contains("Unsupported type"))
  }
}
