package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder
import java.time.LocalDate

@Builder
class RankResponse(
    @JsonProperty("rank")
    val rank: Long,
    @JsonProperty("date")
    val date: LocalDate,
)
