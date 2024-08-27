package linter

data class LinterResult(private val isValid: Boolean, private val message: String) {
    fun isValid(): Boolean {
        return isValid
    }

    fun getMessage(): String {
        return message
    }
}
