package composite

import exceptions.UsuportedDataTypeExeption

object NodeManager {
    val allExistsDataTypes: MutableSet<String> = mutableSetOf(
        "NUMBER",
        "STRING",
        "INT"

    )
    val allExistsMethodCalls: MutableSet<String> = mutableSetOf(
        "println"
    )

    fun addDataType(dataType: String) {
        if (allExistsDataTypes.contains(dataType)) {
            return
        }
        throw UsuportedDataTypeExeption(dataType)
    }
}
