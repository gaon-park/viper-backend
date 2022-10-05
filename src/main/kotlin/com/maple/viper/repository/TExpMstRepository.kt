package com.maple.viper.repository

import com.maple.viper.entity.TExpMst
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Suppress("EmptyClassBlock")
@Repository
interface TExpMstRepository : JpaRepository<TExpMst, Long> {
}
