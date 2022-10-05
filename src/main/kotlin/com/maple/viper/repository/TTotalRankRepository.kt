package com.maple.viper.repository

import com.maple.viper.entity.TTotalRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
interface TTotalRankRepository : JpaRepository<TTotalRank, Long> {
    @Transactional
    fun deleteByCreatedAt(createdAt: LocalDate)
    fun findFirstByCharacterIdOrderByCreatedAtDesc(characterId: Long): TTotalRank?

    @Query(
        value = "select t from TTotalRank t " +
                "where character_id = :characterId and created_at >= :startDate and created_at <= :endDate"
    )
    fun findByCharacterIdAndCreatedAtBetween(
        @Param("characterId") characterId: Long,
        @Param("startDate") start: LocalDate,
        @Param("endDate") end: LocalDate
    ): List<TTotalRank>
}
