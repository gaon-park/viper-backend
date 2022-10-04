package com.maple.viper.service

import com.maple.viper.dto.response.GetCharacterInfoByAccountIDResponse
import com.maple.viper.entity.TAvatarImgUrl
import com.maple.viper.entity.TCharacter
import com.maple.viper.entity.TExp
import com.maple.viper.entity.TTotalRank
import com.maple.viper.entity.TWorldRank
import com.maple.viper.exception.ViperException
import com.maple.viper.repository.TAvatarImgUrlRepository
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
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
class BatchService(
    private val tUserRepository: TUserRepository,
    private val tCharacterRepository: TCharacterRepository,
    private val tWorldRankRepository: TWorldRankRepository,
    private val tTotalRankRepository: TTotalRankRepository,
    private val tExpRepository: TExpRepository,
    private val tAvatarImgUrlRepository: TAvatarImgUrlRepository,
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

        // 등록된 캐릭터
        val tCharacters = users.associate {
            it.id to tCharacterRepository.findByUserIdAndName(
                it.id ?: 0,
                characterInfos[it.id]?.characterName ?: ""
            )
        }

        // 등록되어 있지 않은 캐릭터
        val unSaved = characterInfos.filter { tCharacters[it.key] == null }
            .mapNotNull { it.key?.let { it1 -> TCharacter.generateInsertModel(it1, it.value) } }

        val data = tCharacters.plus(insertCharacter(unSaved))
            .mapNotNull {
                (it.value?.id ?: throw ViperException("invalid data")) to (characterInfos[it.key]
                    ?: throw ViperException("invalid data"))
            }.toMap()

        deleteData()
        insertData(data)
    }

    @Transactional
    fun insertCharacter(data: List<TCharacter>): Map<Long, TCharacter> {
        logger.info("등록되어있지 않은 캐릭터 등록")
        return tCharacterRepository.saveAll(data).associateBy { it.userId }
    }

    @Transactional
    fun deleteData() {
        logger.info("오늘 날짜로 집계된 데이터를 삭제")
        tWorldRankRepository.deleteByCreatedAt(LocalDate.now())
        tTotalRankRepository.deleteByCreatedAt(LocalDate.now())
        tExpRepository.deleteByCreatedAt(LocalDate.now())
        tAvatarImgUrlRepository.deleteByCreatedAt(LocalDate.now())
    }

    @Transactional
    fun insertData(data: Map<Long, GetCharacterInfoByAccountIDResponse>) {
        logger.info("최신 데이터를 등록")
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
    }
}
