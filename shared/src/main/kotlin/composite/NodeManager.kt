package composite

object NodeManager {
    val allExistsDataTypes: MutableSet<String> = mutableSetOf()
    val allExistsMethodCalls : MutableSet<String> = mutableSetOf()

    init {
        addDataType("NUMBER")
        addDataType("STRING")
        addDataType("INT")

        allExistsMethodCalls.add("println")

    }



    fun addDataType(dataType: String){
        if (allExistsDataTypes.contains(dataType)){
            return;
        }
        allExistsDataTypes.add(dataType)
    }
}