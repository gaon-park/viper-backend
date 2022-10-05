package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder
import java.time.LocalDate

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
data class AnalyzeExpResponse(
    @JsonProperty("completionDate")
    val completionDate: LocalDate?,
    @JsonProperty("error")
    val error: Boolean?,
    @JsonProperty("message")
    val message: String?,
)
