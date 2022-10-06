package com.maple.viper.service

import com.maple.viper.dto.request.AnalyzeRequest
import com.maple.viper.dto.response.AnalyzeExpResponse
import com.maple.viper.dto.response.ErrorResponse
import com.maple.viper.entity.TExp
import com.maple.viper.exception.NotFountException
import com.maple.viper.exception.ViperException
import com.maple.viper.info.DefinitionInfo.Companion.LEV_MAX
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
import com.maple.viper.util.ExpUtil
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate

@Service
class AnalyzeExpService(
    private val tCharacterRepository: TCharacterRepository,
    private val tExpRepository: TExpRepository,
    private val mstService: MSTService,
) {
    companion object {
        const val HUNDRED: Long = 100
    }

    /**
     * currentLev -> targetLev 데이터 분석
     */
    fun analyzeExp(request: AnalyzeRequest): Any {
        val tCharacter = tCharacterRepository.findByUserIdAndRepresentativeFlg(request.userId, true)
            ?: throw NotFountException("캐릭터를 찾지 못했습니다.")
        val analyzeStartDate = request.startDate ?: tCharacter.createdAt
        val analyzeEndDate = request.endDate ?: LocalDate.now()

        val tExps = tCharacter.id?.let {
            tExpRepository.findByCharacterIdAndCreatedAtBetween(it, analyzeStartDate, analyzeEndDate)
                .sortedBy { o -> o.createdAt }
        } ?: emptyList()

        val earnedExpAvg = getEarnedExpAvg(tExps)
        return if (earnedExpAvg != BigDecimal.ZERO) {
            val avgPercent = earnedExpAvg /
                    BigDecimal.valueOf(
                        mstService.expMst[tExps.last().lev + 1]?.exp ?: throw ViperException("invalid data")
                    ) * BigDecimal.valueOf(HUNDRED)
            val remainExp = getRemainExp(tExps.last(), request.targetLev ?: LEV_MAX)
            val remainDays = remainExp.div(earnedExpAvg)

            var resultDate = LocalDate.now().plusDays(remainDays.toLong())
            // 소수점 이하 값이 존재하는 경우 +1 일
            if (BigDecimal.valueOf(remainDays.toLong()) != remainDays) {
                resultDate = resultDate.plusDays(1)
            }

            val expUtil = ExpUtil(mstService)
            val accumulateExp = expUtil.getAccumulateExp(tExps.last().lev) + tExps.last().exp
            val accumulateExpForTargetLev = expUtil.getAccumulateExp(request.targetLev ?: LEV_MAX)


            AnalyzeExpResponse(
                totalDuration = tExps.size - 1,
                avgExp = earnedExpAvg,
                avgExpPercent = avgPercent,
                targetLev = request.targetLev ?: LEV_MAX,
                remainExpForTargetLev = remainExp,
                remainDaysForTargetLev = remainDays,
                expPercentForTargetLev = BigDecimal.valueOf((accumulateExp / accumulateExpForTargetLev) * HUNDRED),
                completionDate = resultDate,
            )
        }
        // 경험치 증가폭이 없는 경우
        else {
            ErrorResponse(
                error = true,
                message = "경험치 데이터가 쌓이지 않아 계산이 불가합니다."
            )
        }
    }

    /**
     * 경험치 증가량 계산
     */
    fun getEarnedExpAvg(
        tExps: List<TExp>,
    ): BigDecimal {
        val mstExp =
            mstService.expMst.filter {
                it.value.targetLev <= tExps.last().lev && it.value.targetLev > tExps.first().lev
            }

        var sum: BigDecimal = BigDecimal.ZERO
        val expMap = tExps.mapIndexed { index, tExp -> index to tExp }.toMap()
        List(tExps.size) { index ->
            if (index != tExps.size - 1) {
                // 레벨업
                val current = expMap[index] ?: throw ViperException("invalid data")
                val after = expMap[index + 1] ?: throw ViperException("invalid data")
                val levDiff = after.lev.minus(current.lev)
                var subSum: Long = 0
                when {
                    levDiff == 0 -> {
                        subSum += (after.exp - current.exp)
                    }
                    levDiff == 1 -> {
                        subSum += ((mstExp[after.lev]?.exp ?: 0) - current.exp + after.exp)
                    }
                    levDiff > 1 -> {
                        mstExp.filter { o -> o.value.targetLev > current.lev && o.value.targetLev < after.lev }
                            .map { subSum += it.value.exp }
                        subSum -= current.exp
                        subSum += after.exp
                    }
                }
                sum += BigDecimal.valueOf(subSum.div(tExps.size - 1))
            }
        }
        return sum
    }

    /**
     * targetLev 까지 남은 경험치 계산
     */
    fun getRemainExp(
        currentTExp: TExp,
        targetLev: Int,
    ): BigDecimal {
        var sum = BigDecimal.ZERO
        mstService.expMst.filter { it.value.targetLev <= targetLev && it.value.targetLev > currentTExp.lev }
            .map { sum += BigDecimal.valueOf(it.value.exp) }

        // 현재 레벨의 경험치 고려
        return sum - BigDecimal.valueOf(currentTExp.exp)
    }
}
