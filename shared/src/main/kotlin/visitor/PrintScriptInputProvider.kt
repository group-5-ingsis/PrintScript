package position.visitor

class PrintScriptInputProvider(private val inputMap: Map<String, String> = emptyMap()) : InputProvider {
    override fun input(name: String?): String? {
        return inputMap[name]
    }

}