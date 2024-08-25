package composite

import exceptions.UsuportedDataTypeExeption

object NodeManager {
    val allExistsDataTypes: MutableSet<String> =
        mutableSetOf(
            "NUMBER",
            "STRING",
            "INT"
        )
    val allExistsMethodCalls: MutableSet<String> =
        mutableSetOf(
            "println"
        )

    fun checkIfExist(dataType: String) {
        if (allExistsDataTypes.contains(dataType)) {
            return
        }
        throw UsuportedDataTypeExeption(dataType)
    }
}
