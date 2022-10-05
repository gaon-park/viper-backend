package com.maple.viper.api

import com.maple.viper.service.TCharacterService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/common")
class CommonController(
    private val tCharacterService: TCharacterService
) {

    @GetMapping("/sidebar")
    fun generate(@RequestParam userId: Long) {
        tCharacterService.getCharacterInfo(userId)
    }
}
