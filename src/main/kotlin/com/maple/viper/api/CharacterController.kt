package com.maple.viper.api

import com.maple.viper.dto.request.HistoryRequest
import com.maple.viper.dto.response.CharacterInfoResponse
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

    @GetMapping("/history")
    fun historyExp(@Valid @RequestBody request: HistoryRequest): ResponseEntity<Map<String, Any>> {
        val map = mutableMapOf<String, Any>()
        map["expHistory"] = historyService.getExpHistory(
            request.userId,
            request.startDate,
            request.endDate,
            request.targetLev
        )
        map["worldRankHistory"] = historyService.getWorldRankHistory(
            request.userId,
            request.startDate,
            request.endDate,
        )
        map["totalRankHistory"] = historyService.getTotalRankHistory(
            request.userId,
            request.startDate,
            request.endDate,
        )
        map["popHistory"] = historyService.getPopHistory(
            request.userId,
            request.startDate,
            request.endDate,
        )
        return ResponseEntity.ok(map)
    }
}
