package visitor

class PrintScriptInputProvider(private val inputMap: Map<String, String> = emptyMap()) : InputProvider {
    override fun input(name: String?): String {
        var input = inputMap[name]
        if (input == null) {
            println(name)
            input = readln()
        }
        return input
    }
}
