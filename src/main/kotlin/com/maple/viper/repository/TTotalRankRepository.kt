package com.maple.viper.repository

import com.maple.viper.entity.TTotalRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
interface TTotalRankRepository : JpaRepository<TTotalRank, Long> {
    @Transactional
    fun deleteByCreatedAt(createdAt: LocalDate)
}
