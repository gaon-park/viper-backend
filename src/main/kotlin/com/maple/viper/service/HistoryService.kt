package com.maple.viper.service

import com.maple.viper.dto.response.ExpResponse
import com.maple.viper.exception.NotFountException
import com.maple.viper.exception.ViperException
import com.maple.viper.info.DefinitionInfo.Companion.LEV_MAX
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HistoryService(
    private val mstService: MSTService,
    private val tExpRepository: TExpRepository,
    private val tCharacterRepository: TCharacterRepository
) {
    fun getExpHistory(
        userId: Long,
        startDate: LocalDate?,
        endDate: LocalDate?,
        targetLev: Int?
    ): List<ExpResponse> {
        val characterId = tCharacterRepository.findByUserIdAndRepresentativeFlg(userId, true)?.id
            ?: throw NotFountException("캐릭터 정보가 존재하지 않음")
        val tExps = tExpRepository.findByCharacterIdAndCreatedAtBetween(
            characterId = characterId,
            start = startDate ?: LocalDate.of(2022, 1, 1),
            end = endDate ?: LocalDate.of(2999, 1, 1)
        )
        val accumulateExpForTargetLev = getAccumulateExp(targetLev ?: LEV_MAX)
        return tExps.map {
            val accumulateExp = getAccumulateExp(it.lev) + it.exp
            val percentForNextLev =
                (it.exp.toDouble() / (mstService.expMst[it.lev + 1]?.exp ?: throw ViperException("invalid data"))) * 100
            val percentForTargetLev = (accumulateExp / accumulateExpForTargetLev) * 100
            ExpResponse(
                lev = it.lev,
                exp = it.exp,
                expPercentForNextLev = percentForNextLev,
                expPercentForTargetLev = percentForTargetLev
            )
        }
    }

    /**
     * targetLev 까지의 누적 경험치 계산
     */
    fun getAccumulateExp(targetLev: Int): Double {
        var result: Double = (0).toDouble()
        mstService.expMst.values.filter { it.targetLev <= targetLev }.forEach { result += it.exp }
        return result
    }
}