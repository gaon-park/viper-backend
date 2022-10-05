package com.maple.viper.api

import com.maple.viper.dto.response.CharacterInfoResponse
import com.maple.viper.service.TCharacterService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/character")
class CharacterController(
    private val tCharacterService: TCharacterService
) {
    @GetMapping
    fun getCharacterInfo(@RequestParam userId: Long): ResponseEntity<CharacterInfoResponse> =
        ResponseEntity.ok(tCharacterService.getCharacterInfo(userId))
}
