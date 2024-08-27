package linter

data class LinterResult(private val isSuccessful: Boolean, private val message: String) {
    fun isValid(): Boolean {
        return isSuccessful
    }

    fun getMessage(): String {
        return message
    }
}
