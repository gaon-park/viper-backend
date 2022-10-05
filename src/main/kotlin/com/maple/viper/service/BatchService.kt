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
        logger.info("오늘 날짜로 집계된 데이터를 삭제")
        deleteData()

        logger.info("DB 검색")
        val users = tUserRepository.findAll().filterNotNull()

        logger.info("soap 검색")
        val jsonUtil = JsonUtil()
        val soapUtil = SoapUtil()
        val characterInfos =
            users.associate {
                (it.id ?: throw ViperException("invalid data")) to jsonUtil.getDataFromNexonJson(
                    soapUtil.getCharacterInfoByAccountID(it.accountId)
                )
            }

        val insertData = mutableMapOf<Long, GetCharacterInfoByAccountIDResponse>()
        characterInfos.map {
            val characterId =
                upsertCharactersByUserId(it.key, it.value) ?: throw ViperException("failed insert t_character")
            insertData[characterId] = it.value
        }

        logger.info("최신 데이터를 등록")
        insertData(insertData)
    }

    fun upsertCharactersByUserId(userId: Long, characterInfo: GetCharacterInfoByAccountIDResponse): Long? {
        logger.info("userId[${userId}] 기존 데이터 갱신")
        val savedList = tCharacterRepository.findByUserId(userId)
        val upsertList = mutableListOf<TCharacter>()
        savedList.firstOrNull { it.name == characterInfo.characterName }?.let {
            upsertList.add(it.copy(representativeFlg = true))
        }
        savedList.filter { it.name != characterInfo.characterName }.map {
            upsertList.add(it.copy(representativeFlg = false))
        }

        return if (savedList.any { it.name == characterInfo.characterName }) {
            upsertCharacter(upsertList).firstOrNull { it.name == characterInfo.characterName }?.id
        } else {
            logger.info("userId[${userId}]에 신규 대표캐릭터를 등록")
            insertCharacter(TCharacter.generateInsertModel(userId, characterInfo)).id
        }
    }

    @Transactional
    fun insertCharacter(data: TCharacter): TCharacter {
        return tCharacterRepository.save(data)
    }

    @Transactional
    fun upsertCharacter(data: List<TCharacter>): List<TCharacter> {
        return tCharacterRepository.saveAll(data)
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
