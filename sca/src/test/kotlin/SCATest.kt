import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import sca.ExecuteSca
import sca.StaticCodeAnalyzerRules
import sca.StaticCodeIssue

class SCATest {

    @Test
    fun `test 001 - should analyze that the type matched`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("", functionArgumentCheck = true, typeMatchingCheck = true, identifierNamingCheck = true)
        val ast =
            listOf(
                Node.AssignationDeclaration(
                    dataType = Node.DataType("NUMBER"),
                    kindVariableDeclaration = "let",
                    identifier = "a",
                    value = Node.GenericLiteral("2",Node.DataType("NUMBER"))
                )
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }

    @Test
    fun `test 002 - should analyze that the type did not match`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("./src/main/resources/sca_rules.yaml")
        val ast =
            listOf(
                Node.AssignationDeclaration(
                    dataType = Node.DataType("NUMBER"),
                    kindVariableDeclaration = "let",
                    identifier = "a",
                    value = Node.GenericLiteral("Hello",Node.DataType("STRING"))
                )
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertEquals(1, issues.size)
        assertEquals("Variable declaration does not match the type of the assigned value", issues[0].message)
    }

    @Test
    fun `test 003 - should analyze that the println argument is valid`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("./src/main/resources/sca_rules.yaml")
        val ast =
            listOf(
                Node.Method(
                    Node.Arguments(listOf(Node.GenericLiteral("2",Node.DataType("STRING")))),
                    Node.Identifier("println")
                ),
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }

    /*@Test
    fun `test 004 - should analyze that the println argument is invalid`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("", functionArgumentCheck = true, typeMatchingCheck = true, identifierNamingCheck = true)
        val ast =
            listOf(
                Node.Method(
                    Node.Arguments(listOf(Node.GenericLiteral("2",Node.DataType("STRING")))),
                    Node.Identifier("println")
                ),
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertEquals(1, issues.size)
        assertEquals("The println function should be called only with an identifier or a literal, the expression: a + world is invalid.", issues[0].message)
    }

     */

    @Test
    fun `test 005 - should analyze that the identifier is not in lower camel case`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("./src/main/resources/sca_rules.yaml")
        val ast =
            listOf(
                Node.AssignationDeclaration(
                    dataType = Node.DataType("STRING"),
                    kindVariableDeclaration = "let",
                    identifier = "my_greetings",
                    value = Node.GenericLiteral("Hello",Node.DataType("STRING"))
                )
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertEquals(1, issues.size)
        assertEquals("The identifier 'my_greetings' must be in lower camel case.", issues[0].message)
    }

    @Test
    fun `test 006 - should analyze that the identifier is in lower camel case`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("./src/main/resources/sca_rules.yaml")
        val ast =
            listOf(
                Node.AssignationDeclaration(
                    dataType = Node.DataType("STRING"),
                    kindVariableDeclaration = "let",
                    identifier = "myGreetings",
                    value = Node.GenericLiteral("Hello",Node.DataType("STRING"))
                )
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }

    @Test
    fun `test 007 - should ignore type matching check when false`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("./src/main/resources/sca_rules2.yaml")
        val ast =
            listOf(
                Node.AssignationDeclaration(
                    dataType = Node.DataType("NUMBER"),
                    kindVariableDeclaration = "let",
                    identifier = "a",
                    value = Node.GenericLiteral("Hello",Node.DataType("STRING"))
                )
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }

    @Test
    fun `test 008 - should ignore identifier naming check when false`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("./src/main/resources/sca_rules3.yaml")
        val ast =
            listOf(
                Node.AssignationDeclaration(
                    dataType = Node.DataType("STRING"),
                    kindVariableDeclaration = "let",
                    identifier = "my_greetings",
                    value = Node.GenericLiteral("Hello",Node.DataType("STRING"))
                )
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }
/*
    @Test
    fun `test 009 - should ignore println argument check when false`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("", functionArgumentCheck = false, typeMatchingCheck = true, identifierNamingCheck = true)
        val ast =
            listOf(
                Method(
                    "println",
                    BinaryOperation(
                        IdentifierOperator("Variable"),
                        "+",
                        NumberOperator(5.0),
                    ),
                ),
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }

    @Test
    fun `test 010 - should analyze that the type matched for number`() {
        val executeSca = ExecuteSca.getDefaultSCA()
        val issues: MutableList<StaticCodeIssue> = mutableListOf()
        val scaRules = StaticCodeAnalyzerRules("", functionArgumentCheck = true, typeMatchingCheck = true, identifierNamingCheck = true)
        val ast =
            listOf(
                DeclarationAssignation(
                    Declaration("a", "number"),
                    NumberOperator(5.0),
                    false,
                ),
                DeclarationAssignation(
                    Declaration("b", "number"),
                    BinaryOperation(
                        NumberOperator(2.0),
                        "+",
                        NumberOperator(3.0),
                    ),
                    false,
                ),
            )
        for (node in ast) {
            issues.addAll(executeSca.analyzeNode(node, scaRules))
        }
        assertTrue(issues.isEmpty())
    }

 */

}