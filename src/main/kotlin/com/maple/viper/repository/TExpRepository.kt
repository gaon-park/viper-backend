package com.maple.viper.repository

import com.maple.viper.entity.TExp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
interface TExpRepository : JpaRepository<TExp, Long> {
    @Transactional
    fun deleteByCreatedAt(createdAt: LocalDate)
    fun findFirstByCharacterIdOrderByCreatedAtDesc(characterId: Long): TExp?
}
