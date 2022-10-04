package com.maple.viper.api

import com.maple.viper.service.BatchService
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/batch")
class BatchAPI(
    private val batchService: BatchService
) {

    @PutMapping("/data")
    fun update(): String {
        batchService.update()
        return "ok"
    }
}
