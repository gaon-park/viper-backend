package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder
import java.time.LocalDate

@Builder
class PopResponse(
    @JsonProperty("pop")
    val pop: Int,
    @JsonProperty("date")
    val date: LocalDate,
)
