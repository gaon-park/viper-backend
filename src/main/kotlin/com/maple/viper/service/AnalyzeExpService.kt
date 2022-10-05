package com.maple.viper.service

import com.maple.viper.entity.TExp
import com.maple.viper.entity.TExpMst
import com.maple.viper.exception.NotFountException
import com.maple.viper.info.ExpPortionInfo
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpMstRepository
import com.maple.viper.repository.TExpRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.time.temporal.ChronoUnit

@Service
class AnalyzeExpService(
    private val tCharacterRepository: TCharacterRepository,
    private val tExpMstRepository: TExpMstRepository,
    private val tExpRepository: TExpRepository
) {
    companion object {
        private const val LEV_MAX = 300
    }

    private val expMst: Map<Int, TExpMst> by lazy {
        tExpMstRepository.findAll().associateBy { it.targetLev }
    }

    /**
     * currentLev -> targetLev 달성일 계산
     */
    fun analyzeExp(
        userId: Long, startDate: LocalDate?, endDate: LocalDate?, targetLev: Int?, portionMap: Map<ExpPortionInfo, Int>
    ): LocalDate {
        tCharacterRepository.findByUserIdAndRepresentativeFlg(userId, true)?.let {
            val analyzeStartDate = startDate ?: it.createdAt
            val analyzeEndDate = endDate ?: LocalDate.now()

            val tExps = it.id?.let { it1 ->
                tExpRepository.findByCharacterIdAndCreatedAtBetween(
                    it1, analyzeStartDate, analyzeEndDate
                ).sortedByDescending { o -> o.createdAt }
            } ?: emptyList()

            val earnedExp = BigDecimal.valueOf((tExps.first().exp - tExps.last().exp))
            // 경험치 증가폭이 없는 경우
            if (earnedExp == BigDecimal.ZERO) {
                return LocalDate.MAX
            } else {
                val avgExp: BigDecimal = earnedExp.minus(getPortionExp(portionMap))
                    .div(BigDecimal.valueOf(ChronoUnit.DAYS.between(analyzeStartDate, analyzeEndDate) + 1))
                val remainExp = getRemainExp(tExps.first(), targetLev ?: LEV_MAX)
                val remainDays = remainExp.div(avgExp)

                var resultDate = LocalDate.now().plusDays(remainDays.toLong())
                // 소수점 이하 값이 존재하는 경우 +1 일
                if (BigDecimal.valueOf(remainDays.toLong()) != remainDays) {
                    resultDate = resultDate.plusDays(1)
                }
                return resultDate
            }
        } ?: throw NotFountException("캐릭터를 찾지 못했습니다.")
    }

    /**
     * targetLev 까지 남은 경험치 계산
     */
    fun getRemainExp(
        currentTExp: TExp,
        targetLev: Int,
    ): BigDecimal {
        var remainExp: BigDecimal = BigDecimal.ZERO
        expMst.filter { it.value.targetLev >= targetLev && it.value.targetLev > currentTExp.lev }
            .map { remainExp += BigDecimal.valueOf(it.value.exp) }

        // 현재 레벨의 경험치 고려
        return remainExp - BigDecimal.valueOf(currentTExp.exp)
    }

    /**
     * 비약으로 얻은 경험치 총합
     */
    fun getPortionExp(
        portionMap: Map<ExpPortionInfo, Int>
    ): BigDecimal {
        var exp: BigDecimal = BigDecimal.ZERO
        portionMap.map {
            exp += BigDecimal.valueOf(expMst[it.key.id]?.exp?.times(it.key.id) ?: 0)
        }
        return exp
    }
}
