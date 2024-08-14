package composite.types

import composite.Node
import composite.ResultType
import exceptions.InvalidMethodException
import visitor.NodeResult
import visitor.NodeVisitor

class MethodCall(private val identifier: Identifier, private val arguments: Arguments) : Node {
  private val builtInMethods: List<String> = listOf("println")

  /* Possibilities for arguments: Number, String, Identifier. */
  @Throws(InvalidMethodException::class)
  override fun solve(): NodeResult {
    
    val method = identifier.solve()
    val args = arguments.solve()

    val methodName = method.primaryValue.toString()

    val isBuiltInMethod = isBuiltInMethod(methodName)
    if (!isBuiltInMethod) {

      throw InvalidMethodException("Unexpected method call: ${method.primaryValue}")
    }

    return NodeResult(ResultType.METHOD_CALL, method, args)
  }

  override fun accept(visitor: NodeVisitor) {
    return visitor.visitMethodCall(this)
  }

  private fun isBuiltInMethod(name: String) : Boolean {
    return builtInMethods.contains(name)
  }
}