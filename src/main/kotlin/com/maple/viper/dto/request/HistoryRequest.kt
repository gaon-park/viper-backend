package com.maple.viper.dto.request

import java.time.LocalDate
import javax.validation.constraints.NotNull

data class HistoryRequest(
    @NotNull(message = "userId는 필수항목입니다.")
    val userId: Long,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val targetLev: Int?,
)
