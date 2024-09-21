package linter

data class LinterResult(private val isSuccessful: Boolean, val message: String) {
    fun isValid(): Boolean {
        return isSuccessful
    }
}
