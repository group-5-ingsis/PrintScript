package composite.types

import composite.Node
import composite.ResultType
import exceptions.InvalidMethodException
import visitor.NodeResult
import visitor.NodeVisitor

class MethodCall(private val identifier: Node, private val arguments: List<Node>) : Node {
  private val builtInMethods: List<String> = listOf("println")

  override fun solve(): NodeResult {
    val method = identifier.solve()
    if (isBuiltInMethod(method.primaryValue.toString())) {
      throw InvalidMethodException("Unexpected method call: ${method.primaryValue}")
    }
    return NodeResult(ResultType.METHOD_CALL, method, arguments)
  }

  override fun accept(visitor: NodeVisitor) {
    TODO("Not yet implemented")
  }

  private fun isBuiltInMethod(name: String) : Boolean {
    return builtInMethods.contains(name)
  }
}