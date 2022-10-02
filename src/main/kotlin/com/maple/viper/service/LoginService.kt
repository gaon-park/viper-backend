package com.maple.viper.service

import com.maple.viper.exception.ViperException
import com.maple.viper.repository.TUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val tUserRepository: TUserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String?): UserDetails = username?.let {
        tUserRepository.findByEmail(it)
    } ?: throw ViperException("does not exist")
}
