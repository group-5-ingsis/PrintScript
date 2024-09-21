package builder

import builder.command.AnalyzeCommandBuilder
import builder.command.ExecuteCommandBuilder
import builder.command.FormattingCommandBuilder
import builder.command.ValidationCommandBuilder

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
