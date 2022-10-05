package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder

@Builder
data class LoginResponse(
    @JsonProperty("jwtToken")
    val jwtToken: String?,
)
