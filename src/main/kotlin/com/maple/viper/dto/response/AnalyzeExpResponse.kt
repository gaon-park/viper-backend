package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.Builder
import java.math.BigDecimal
import java.time.LocalDate

@Builder
data class AnalyzeExpResponse(
    @JsonProperty("total_duration")
    val totalDuration: Int,
    @JsonProperty("avg_exp")
    val avgExp: BigDecimal,
    @JsonProperty("avg_exp_percent")
    val avgExpPercent: BigDecimal,
    @JsonProperty("target_lev")
    val targetLev: Int,
    @JsonProperty("remain_exp_for_target_lev")
    val remainExpForTargetLev: BigDecimal,
    @JsonProperty("remain_days_for_target_lev")
    val remainDaysForTargetLev: BigDecimal,
    @JsonProperty("exp_percent_for_target_lev")
    val expPercentForTargetLev: BigDecimal,
    @JsonProperty("completion_date")
    val completionDate: LocalDate,
)
