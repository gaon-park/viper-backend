package com.maple.viper.service

import com.maple.viper.entity.TUser
import com.maple.viper.form.UserRegistForm
import com.maple.viper.repository.TUserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class TUserService(
    private val tUserRepository: TUserRepository
) {
    @Transactional
    fun insert(form: UserRegistForm) {
        tUserRepository.save(TUser.generateInsertModel(form))
    }
}
