package parser

import parser.composite.ResultType

data class NodeResult(val type: ResultType, val primaryValue: Any, val secondaryValue: Any?)