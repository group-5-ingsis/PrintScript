package interpreter

class PrintScriptInputProvider(private val inputMap: Map<String, String>) : InputProvider {
    override fun input(name: String?): String? {
        return inputMap[name]
    }
    constructor() {
        this.inputMap = emptyMap() // Mapa vacío por defecto
    }
}