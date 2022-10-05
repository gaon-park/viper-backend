package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
data class LoginResponse(
    @JsonProperty("jwtToken")
    val jwtToken: String?,
    @JsonProperty("message")
    val message: String?,
)
