package com.maple.viper.service

import com.maple.viper.entity.TUser
import com.maple.viper.exception.ViperException
import com.maple.viper.form.UserRegistForm
import com.maple.viper.repository.TUserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TUserService(
    private val tUserRepository: TUserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun insert(form: UserRegistForm) {
        if (tUserRepository.findByEmail(form.email) != null) {
            throw ViperException("already exist")
        }
        tUserRepository.save(TUser.generateInsertModel(form.email, passwordEncoder.encode(form.password)))
    }
}
