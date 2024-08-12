package visitor

import composite.types.ResultType

data class NodeResult(val type: ResultType, val primaryValue: Any, val secondaryValue: Any?)