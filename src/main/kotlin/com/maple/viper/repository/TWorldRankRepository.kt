package com.maple.viper.repository

import com.maple.viper.entity.TWorldRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
interface TWorldRankRepository : JpaRepository<TWorldRank, Long> {
    @Transactional
    fun deleteByCreatedAt(createdAt: LocalDate)
}
