package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder
import java.time.LocalDate

@Builder
data class ExpResponse(
    @JsonProperty("lev")
    val lev: Int,
    @JsonProperty("exp")
    val exp: Long,
    @JsonProperty("target_lev")
    val targetLev: Int,
    @JsonProperty("exp_percent_for_next_lev")
    val expPercentForNextLev: Double,
    @JsonProperty("exp_percent_for_target_lev")
    val expPercentForTargetLev: Double,
    @JsonProperty("date")
    val date: LocalDate,
)
