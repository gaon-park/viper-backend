package com.maple.viper.repository

import com.maple.viper.entity.TExp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
interface TExpRepository : JpaRepository<TExp, Long> {
    @Transactional
    fun deleteByCreatedAt(createdAt: LocalDate)
    fun findFirstByCharacterIdOrderByCreatedAtDesc(characterId: Long): TExp?

    @Query(
        value = "select t from TExp t " +
                "where character_id = :characterId and created_at >= :startDate and created_at <= :endDate"
    )
    fun findByCharacterIdAndCreatedAtBetween(
        @Param("characterId") characterId: Long,
        @Param("startDate") start: LocalDate,
        @Param("endDate") end: LocalDate
    ): List<TExp>
}
