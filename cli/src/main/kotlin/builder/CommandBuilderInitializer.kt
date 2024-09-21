package builder

object CommandBuilderInitializer {

    fun getValidBuilders(): Map<String, CommandBuilder> {
        return mapOf(
            "validate" to ValidationCommandBuilder(),
            "execute" to ExecuteCommandBuilder(),
            "format" to FormattingCommandBuilder(),
            "analyze" to AnalyzeCommandBuilder()
        )
    }
}
