package com.maple.viper.repository

import com.maple.viper.entity.TExp
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("EmptyClassBlock")
@Repository
interface TExpRepository : JpaRepository<TExp, Long> {
}
