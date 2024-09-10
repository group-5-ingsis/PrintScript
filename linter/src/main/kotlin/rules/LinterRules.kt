package rules

// Marker Interface
interface LinterRules {
    fun getAsMap(): Map<String, Any>
    fun getAsJson(): String {
        return getAsMap().toString()
    }
}
