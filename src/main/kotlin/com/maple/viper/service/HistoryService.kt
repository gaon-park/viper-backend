package com.maple.viper.service

import com.maple.viper.dto.response.ExpResponse
import com.maple.viper.dto.response.PopResponse
import com.maple.viper.dto.response.RankResponse
import com.maple.viper.exception.NotFountException
import com.maple.viper.exception.ViperException
import com.maple.viper.info.DefinitionInfo.Companion.LEV_MAX
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
import com.maple.viper.repository.TPopRepository
import com.maple.viper.repository.TTotalRankRepository
import com.maple.viper.repository.TWorldRankRepository
import com.maple.viper.util.ExpUtil
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class HistoryService(
    private val mstService: MSTService,
    private val tExpRepository: TExpRepository,
    private val tCharacterRepository: TCharacterRepository,
    private val tWorldRankRepository: TWorldRankRepository,
    private val tTotalRankRepository: TTotalRankRepository,
    private val tPopRepository: TPopRepository
) {
    companion object {
        const val HUNDRED: Int = 100
        val DEFAULT_START_DATE: LocalDate = LocalDate.of(2022, 1, 1)
        val DEFAULT_END_DATE: LocalDate = LocalDate.of(2999, 1, 1)
    }

    /**
     * userId에 연결된 대표캐릭터의 ID를 검색
     */
    fun getCharacterId(userId: Long) =
        tCharacterRepository.findByUserIdAndRepresentativeFlg(userId, true)?.id
            ?: throw NotFountException("캐릭터 정보가 존재하지 않음")

    /**
     * 기간 내의 경험치 히스토리 검색
     */
    fun getExpHistory(
        userId: Long, startDate: LocalDate?, endDate: LocalDate?, targetLev: Int?
    ): List<ExpResponse> {
        val characterId = getCharacterId(userId)
        val tExps = tExpRepository.findByCharacterIdAndCreatedAtBetween(
            characterId = characterId, start = startDate ?: DEFAULT_START_DATE, end = endDate ?: DEFAULT_END_DATE
        )
        val expUtil = ExpUtil(mstService)
        val accumulateExpForTargetLev = expUtil.getAccumulateExp(targetLev ?: LEV_MAX)
        return tExps.map {
            val accumulateExp = expUtil.getAccumulateExp(it.lev) + it.exp
            val percentForNextLev = (it.exp.toDouble() / (mstService.expMst[it.lev + 1]?.exp
                ?: throw ViperException("invalid data"))) * HUNDRED
            val percentForTargetLev = (accumulateExp / accumulateExpForTargetLev) * HUNDRED
            ExpResponse(
                lev = it.lev,
                exp = it.exp,
                targetLev = targetLev ?: LEV_MAX,
                expPercentForNextLev = percentForNextLev,
                expPercentForTargetLev = percentForTargetLev,
                date = it.createdAt
            )
        }
    }

    /**
     * 기간 내의 전체랭킹 히스토리 검색
     */
    fun getTotalRankHistory(
        userId: Long, startDate: LocalDate?, endDate: LocalDate?
    ): List<RankResponse> = tTotalRankRepository.findByCharacterIdAndCreatedAtBetween(
        characterId = getCharacterId(userId), start = startDate ?: DEFAULT_START_DATE, end = endDate ?: DEFAULT_END_DATE
    ).map { RankResponse(rank = it.ranking, date = it.createdAt) }

    /**
     * 기간 내의 월드랭킹 히스토리 검색
     */
    fun getWorldRankHistory(
        userId: Long, startDate: LocalDate?, endDate: LocalDate?
    ): List<RankResponse> = tWorldRankRepository.findByCharacterIdAndCreatedAtBetween(
        characterId = getCharacterId(userId), start = startDate ?: DEFAULT_START_DATE, end = endDate ?: DEFAULT_END_DATE
    ).map { RankResponse(rank = it.ranking, date = it.createdAt) }

    fun getPopHistory(
        userId: Long, startDate: LocalDate?, endDate: LocalDate?
    ): List<PopResponse> = tPopRepository.findByCharacterIdAndCreatedAtBetween(
        characterId = getCharacterId(userId), start = startDate ?: DEFAULT_START_DATE, end = endDate ?: DEFAULT_END_DATE
    ).map { PopResponse(pop = it.pop, date = it.createdAt) }
}
