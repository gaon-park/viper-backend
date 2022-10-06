package com.maple.viper.service

import com.maple.viper.dto.request.UserRegistRequest
import com.maple.viper.entity.TAvatarImgUrl
import com.maple.viper.entity.TCharacter
import com.maple.viper.entity.TExp
import com.maple.viper.entity.TPop
import com.maple.viper.entity.TTotalRank
import com.maple.viper.entity.TUser
import com.maple.viper.entity.TWorldRank
import com.maple.viper.exception.AlreadyExistException
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
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/**
 * 데이터베이스 전반 등록 로직
 */
@Service
@Suppress("LongParameterList")
class RegistService(
    private val passwordEncoder: PasswordEncoder,
    private val tUserRepository: TUserRepository,
    private val tCharacterRepository: TCharacterRepository,
    private val tWorldRankRepository: TWorldRankRepository,
    private val tTotalRankRepository: TTotalRankRepository,
    private val tExpRepository: TExpRepository,
    private val tAvatarImgUrlRepository: TAvatarImgUrlRepository,
    private val tPopRepository: TPopRepository,
) {
    /**
     * 유저 등록 후, 대표캐릭터 정보 검색/등록
     * 대표캐릭터 정보 검색에 실패, DB에 등록하지 못했을 경우 false
     */
    @Transactional
    fun insert(request: UserRegistRequest): Boolean {
        if (tUserRepository.findByEmail(request.email) != null) {
            throw AlreadyExistException("already exist email [${request.email}]")
        }
        return upsertCharacterInfo(tUserRepository.save(TUser.generateInsertModel(request, passwordEncoder)))
    }

    /**
     * 대표캐릭터 정보 검색/등록
     * 대표캐릭터 정보 검색에 실패, DB에 등록하지 못했을 경우 false
     */
    @Transactional
    fun upsertCharacterInfo(tUser: TUser): Boolean {
        val characterInfo = JsonUtil().getDataFromNexonJson(SoapUtil().getCharacterInfoByAccountID(tUser.accountId))
        if (characterInfo.characterName.isNotEmpty()) {
            val tCharacter = TCharacter.generateInsertModel(
                tUser.id ?: throw ViperException("invalid data"), characterInfo
            )
            // 다른 캐릭터를 대표캐릭터로 등록했다가, 변경한 경우
            val already = tCharacterRepository.findByUserId(tUser.id)
            val characterId: Long?
            if (already.isNotEmpty()) {
                // 변경한 대표캐릭터가 등록했던 캐릭터인 경우
                val alreadyC = already.firstOrNull { it.name == characterInfo.characterName }
                val updateChars = already.filter { it.name != characterInfo.characterName }
                characterId = if (alreadyC != null) {
                    tCharacterRepository.save(alreadyC.copy(representativeFlg = true))
                    alreadyC.id
                } else {
                    tCharacterRepository.save(tCharacter).id
                }
                tCharacterRepository.saveAll(updateChars.map { it.copy(representativeFlg = false) })
            } else {
                characterId = tCharacterRepository.save(tCharacter).id
            }

            characterId?.let { id ->
                tWorldRankRepository.save(TWorldRank.generateInsertModel(id, characterInfo.worldRank))
                tTotalRankRepository.save(TTotalRank.generateInsertModel(id, characterInfo.totRank))
                tExpRepository.save(TExp.generateInsertModel(id, characterInfo.lev, characterInfo.exp))
                tAvatarImgUrlRepository.save(TAvatarImgUrl.generateInsertModel(id, characterInfo.avatarImgURL))
                tPopRepository.save(TPop.generateInsertModel(id, characterInfo.pop))
            }
            return true
        }
        return false
    }
}
