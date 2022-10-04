package com.maple.viper.service

import com.maple.viper.entity.TCharacter
import com.maple.viper.entity.TExp
import com.maple.viper.entity.TTotalRank
import com.maple.viper.entity.TUser
import com.maple.viper.entity.TWorldRank
import com.maple.viper.exception.AlreadyExistException
import com.maple.viper.exception.ViperException
import com.maple.viper.form.UserRegistForm
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
import com.maple.viper.repository.TTotalRankRepository
import com.maple.viper.repository.TUserRepository
import com.maple.viper.repository.TWorldRankRepository
import com.maple.viper.util.JsonUtil
import com.maple.viper.util.SoapUtil
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 데이터베이스 전반 등록 로직
 */
@Service
class IndexService(
    private val passwordEncoder: PasswordEncoder,
    private val tUserRepository: TUserRepository,
    private val tCharacterRepository: TCharacterRepository,
    private val tWorldRankRepository: TWorldRankRepository,
    private val tTotalRankRepository: TTotalRankRepository,
    private val tExpRepository: TExpRepository,
) {
    /**
     * 유저 등록 후, 대표캐릭터 정보 검색/등록
     * 대표캐릭터 정보 검색에 실패, DB에 등록하지 못했을 경우 false
     */
    @Transactional
    fun insert(form: UserRegistForm): Boolean {
        if (tUserRepository.findByEmail(form.email) != null) {
            throw AlreadyExistException("already exist")
        }
        return insertCharacterInfo(tUserRepository.save(TUser.generateInsertModel(form, passwordEncoder)))
    }

    /**
     * 대표캐릭터 정보 검색/등록
     * 대표캐릭터 정보 검색에 실패, DB에 등록하지 못했을 경우 false
     */
    @Transactional
    fun insertCharacterInfo(tUser: TUser): Boolean {
        val characterInfo = JsonUtil().getDataFromNexonJson(SoapUtil().getCharacterInfoByAccountID(tUser.accountId))
        if (characterInfo.characterName.isNotEmpty()) {
            val tCharacter = TCharacter.generateInsertModel(
                tUser.id ?: throw ViperException("invalid data"), characterInfo
            )
            tCharacterRepository.save(tCharacter).id?.let { characterId ->
                tWorldRankRepository.save(TWorldRank.generateInsertModel(characterId, characterInfo.worldRank))
                tTotalRankRepository.save(TTotalRank.generateInsertModel(characterId, characterInfo.totRank))
                tExpRepository.save(TExp.generateInsertModel(characterId, characterInfo.lev, characterInfo.exp))
            }
            return true
        }
        return false
    }
}
