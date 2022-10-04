package com.maple.viper.repository

import com.maple.viper.entity.TCharacter
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("EmptyClassBlock")
@Repository
interface TCharacterRepository : JpaRepository<TCharacter, Long> {
}
