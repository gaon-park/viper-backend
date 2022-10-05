package com.maple.viper.service

import com.maple.viper.entity.TExpMst
import com.maple.viper.repository.TExpMstRepository
import org.springframework.stereotype.Service

@Service
class MSTService(
    private val tExpMstRepository: TExpMstRepository,
) {
    val expMst: Map<Int, TExpMst> by lazy {
        tExpMstRepository.findAll().associateBy { it.targetLev }
    }
}