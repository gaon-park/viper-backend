package com.maple.viper.api

import com.maple.viper.dto.request.AnalyzeRequest
import com.maple.viper.service.AnalyzeExpService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/analyze")
class AnalyzeController(
    private val analyzeExpService: AnalyzeExpService
) {
    @GetMapping("/exp")
    fun analyzeExp(@RequestBody request: AnalyzeRequest): ResponseEntity<Any> {
        return ResponseEntity.ok(analyzeExpService.analyzeExp(request))
    }
}
