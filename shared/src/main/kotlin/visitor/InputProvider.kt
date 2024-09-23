package interpreter

interface InputProvider {
    fun input(name: String?): String?
}