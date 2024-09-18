package rules

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class FormattingRules @JsonCreator constructor(
    @JsonProperty("spaceBeforeColon") val spaceBeforeColon: Boolean,
    @JsonProperty("spaceAfterColon") val spaceAfterColon: Boolean,
    @JsonProperty("spaceAroundAssignment") val spaceAroundAssignment: Boolean,
    @JsonProperty("newlineAfterPrintln") val newlineAfterPrintln: Int,
    @JsonProperty("blockIndentation") val blockIndentation: Int,
    @JsonProperty("if-brace-same-line") val ifBraceSameLine: Boolean,
    @JsonProperty("mandatory-single-space-separation") val singleSpaceSeparation: Boolean
)
