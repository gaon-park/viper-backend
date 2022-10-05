package com.maple.viper.api

import com.maple.viper.dto.response.AnalyzeExpResponse
import com.maple.viper.service.AnalyzeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/analyze")
class AnalyzeController(
    private val analyzeService: AnalyzeService
) {
    @GetMapping("/exp")
    fun analyzeExp(@RequestParam userId: Long): ResponseEntity<AnalyzeExpResponse> {
        val completionDate = analyzeService.analyzeExp(
            userId = userId,
            startDate = null,
            endDate = null,
            targetLev = null,
            portionMap = emptyMap()
        )
        return ResponseEntity.ok(
            when (completionDate) {
                LocalDate.MAX ->
                    AnalyzeExpResponse(null, true, "경험치 데이터가 쌓이지 않아 계산에 실패했습니다.")
                else -> AnalyzeExpResponse(completionDate, null, null)
            }
        )
    }
}
