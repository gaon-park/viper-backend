package com.maple.viper.api

import com.maple.viper.dto.request.HistoryRequest
import com.maple.viper.dto.response.CharacterInfoResponse
import com.maple.viper.dto.response.ExpResponse
import com.maple.viper.service.HistoryService
import com.maple.viper.service.TCharacterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/character")
class CharacterController(
    private val tCharacterService: TCharacterService,
    private val historyService: HistoryService
) {
    @GetMapping
    fun getCharacterInfo(@RequestParam userId: Long): ResponseEntity<CharacterInfoResponse> =
        ResponseEntity.ok(tCharacterService.getCharacterInfo(userId))

    @GetMapping("/history/exp")
    fun historyExp(@Valid @RequestBody request: HistoryRequest): ResponseEntity<List<ExpResponse>> {
        return ResponseEntity.ok(
            historyService.getExpHistory(
                request.userId,
                request.startDate,
                request.endDate,
                request.targetLev
            )
        )
    }
}
