package com.maple.viper.util

import com.maple.viper.service.MSTService

class ExpUtil(
    private val mstService: MSTService
) {

    /**
     * targetLev 까지의 누적 경험치 계산
     */
    fun getAccumulateExp(targetLev: Int): Double {
        var result: Double = (0).toDouble()
        mstService.expMst.values.filter { it.targetLev <= targetLev }.forEach { result += it.exp }
        return result
    }
}
