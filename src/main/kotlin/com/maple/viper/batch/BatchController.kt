package com.maple.viper.batch

import com.maple.viper.service.BatchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/batch")
class BatchController(
    private val batchService: BatchService
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    @PutMapping("/data")
    fun update(): String {
        logger.info("최신 데이터 등록 배치 시작")
        batchService.update()
        logger.info("최신 데이터 등록 배치 완료")
        return "ok"
    }
}
