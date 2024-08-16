package visitor

import composite.NodeType

data class NodeResult(val type: NodeType, val primaryValue: Any, val secondaryValue: Any?)
