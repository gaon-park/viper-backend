package com.maple.viper.repository

import com.maple.viper.entity.TWorldRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("EmptyClassBlock")
@Repository
interface TWorldRankRepository : JpaRepository<TWorldRank, Long> {
}
