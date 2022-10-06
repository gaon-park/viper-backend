package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder

@Builder
data class ErrorResponse(
    @JsonProperty("error")
    val error: Boolean,
    @JsonProperty("message")
    val message: String,
)
