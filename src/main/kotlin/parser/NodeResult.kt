package parser

import parser.composite.types.ResultType

data class NodeResult(val type: ResultType, val primaryValue: Any, val secondaryValue: Any?) {
    fun getType(): ResultType {
        return type
    }
    fun getValueA(): Any {
        return primaryValue
    }
    fun getValueB(): Any? {
        return secondaryValue
    }
}