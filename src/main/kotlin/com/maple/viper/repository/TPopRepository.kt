package com.maple.viper.repository

import com.maple.viper.entity.TPop
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.transaction.Transactional

@Repository
interface TPopRepository : JpaRepository<TPop, Long> {
    @Transactional
    fun deleteByCreatedAt(createdAt: LocalDate)
    fun findFirstByCharacterIdOrderByCreatedAtDesc(characterId: Long): TPop?
}
