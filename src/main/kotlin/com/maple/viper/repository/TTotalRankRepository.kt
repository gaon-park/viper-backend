package com.maple.viper.repository

import com.maple.viper.entity.TTotalRank
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("EmptyClassBlock")
@Repository
interface TTotalRankRepository : JpaRepository<TTotalRank, Long> {
}
