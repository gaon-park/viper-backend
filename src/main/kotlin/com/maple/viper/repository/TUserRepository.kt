package com.maple.viper.repository

import com.maple.viper.entity.TUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TUserRepository : JpaRepository<TUser, Long> {
    fun findByEmail(email: String): TUser?
}
