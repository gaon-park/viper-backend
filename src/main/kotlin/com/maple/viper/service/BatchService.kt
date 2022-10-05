package com.maple.viper.service

import com.maple.viper.dto.response.GetCharacterInfoByAccountIDResponse
import com.maple.viper.entity.TAvatarImgUrl
import com.maple.viper.entity.TCharacter
import com.maple.viper.entity.TExp
import com.maple.viper.entity.TPop
import com.maple.viper.entity.TTotalRank
import com.maple.viper.entity.TWorldRank
import com.maple.viper.exception.ViperException
import com.maple.viper.repository.TAvatarImgUrlRepository
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
import com.maple.viper.repository.TPopRepository
import com.maple.viper.repository.TTotalRankRepository
import com.maple.viper.repository.TUserRepository
import com.maple.viper.repository.TWorldRankRepository
import com.maple.viper.util.JsonUtil
import com.maple.viper.util.SoapUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.transaction.Transactional

/**
 * BatchAPI - Service
 */
@Service
@Suppress("LongParameterList")
class BatchService(
    private val tUserRepository: TUserRepository,
    private val tCharacterRepository: TCharacterRepository,
    private val tWorldRankRepository: TWorldRankRepository,
    private val tTotalRankRepository: TTotalRankRepository,
    private val tExpRepository: TExpRepository,
    private val tAvatarImgUrlRepository: TAvatarImgUrlRepository,
    private val tPopRepository: TPopRepository,
) {
    val logger: Logger = LoggerFactory.getLogger(this.javaClass.simpleName)

    fun update() {
        logger.info("DB 검색")
        val users = tUserRepository.findAll().filterNotNull()

        logger.info("soap 검색")
        val jsonUtil = JsonUtil()
        val soapUtil = SoapUtil()
        val characterInfos =
            users.associate {
                it.id to jsonUtil.getDataFromNexonJson(
                    soapUtil.getCharacterInfoByAccountID(it.accountId)
                )
            }

        // 등록된 대표캐릭터
        val savedCharacters =
            tCharacterRepository.findByUserIdIn(users.mapNotNull { it.id }).filter { it.representativeFlg }

        // 등록된 캐릭터가 동일한 경우
        val sameCharacters = savedCharacters.filter { it.name == characterInfos[it.userId]?.characterName }
            .associateBy { it.userId }

        // 등록된 캐릭터가 동일하지 않은 경우(대표캐릭터 변경)
        val diffCharacters = savedCharacters.filter { it.name != characterInfos[it.userId]?.characterName }
        if (diffCharacters.isNotEmpty()) {
            logger.info("대표캐릭터가 변경되어 기존 캐릭터의 representativeFlg 를 False 로 갱신")
            upsertCharacter(diffCharacters.map { it.copy(representativeFlg = false) })
        }

        // 대표 캐릭터가 새로 등록된 경우(신규 등록)
        val unSaved = characterInfos.filter { sameCharacters[it.key] == null }
            .mapNotNull { it.key?.let { it1 -> TCharacter.generateInsertModel(it1, it.value) } }

        val data = with(unSaved) {
            if (unSaved.isNotEmpty()) {
                logger.info("등록되어있지 않은 캐릭터 등록")
                sameCharacters.plus(upsertCharacter(unSaved))
            } else {
                sameCharacters
            }
        }.mapNotNull {
            (it.value.id ?: throw ViperException("invalid data")) to (characterInfos[it.key]
                ?: throw ViperException("invalid data"))
        }.toMap()

        logger.info("오늘 날짜로 집계된 데이터를 삭제")
        deleteData()
        logger.info("최신 데이터를 등록")
        insertData(data)
    }

    @Transactional
    fun upsertCharacter(data: List<TCharacter>): Map<Long, TCharacter> {
        return tCharacterRepository.saveAll(data).associateBy { it.userId }
    }

    @Transactional
    fun deleteData() {
        tWorldRankRepository.deleteByCreatedAt(LocalDate.now())
        tTotalRankRepository.deleteByCreatedAt(LocalDate.now())
        tExpRepository.deleteByCreatedAt(LocalDate.now())
        tAvatarImgUrlRepository.deleteByCreatedAt(LocalDate.now())
        tPopRepository.deleteByCreatedAt(LocalDate.now())
    }

    @Transactional
    fun insertData(data: Map<Long, GetCharacterInfoByAccountIDResponse>) {
        tWorldRankRepository.saveAll(data.map {
            TWorldRank.generateInsertModel(it.key, it.value.worldRank)
        })
        tTotalRankRepository.saveAll(data.map {
            TTotalRank.generateInsertModel(it.key, it.value.totRank)
        })
        tExpRepository.saveAll(data.map {
            TExp.generateInsertModel(it.key, it.value.lev, it.value.exp)
        })
        tAvatarImgUrlRepository.saveAll(data.map {
            TAvatarImgUrl.generateInsertModel(it.key, it.value.avatarImgURL)
        })
        tPopRepository.saveAll(data.map {
            TPop.generateInsertModel(it.key, it.value.pop)
        })
    }
}
