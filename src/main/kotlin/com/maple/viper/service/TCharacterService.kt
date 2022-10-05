package com.maple.viper.service

import com.maple.viper.dto.response.GetCharacterInfoByAccountIDResponse
import com.maple.viper.exception.ViperException
import com.maple.viper.repository.TAvatarImgUrlRepository
import com.maple.viper.repository.TCharacterRepository
import com.maple.viper.repository.TExpRepository
import com.maple.viper.repository.TPopRepository
import com.maple.viper.repository.TTotalRankRepository
import com.maple.viper.repository.TWorldRankRepository
import org.springframework.stereotype.Service

@Service
class TCharacterService(
    private val tCharacterRepository: TCharacterRepository,
    private val tAvatarImgUrlRepository: TAvatarImgUrlRepository,
    private val tExpRepository: TExpRepository,
    private val tPopRepository: TPopRepository,
    private val tTotalRankRepository: TTotalRankRepository,
    private val tWorldRankRepository: TWorldRankRepository,
) {
    fun getCharacterInfo(userId: Long): GetCharacterInfoByAccountIDResponse {
        tCharacterRepository.findByUserIdAndRepresentativeFlg(userId, true)?.let {
            val character = it
            it.id?.let { characterId ->
                val avatarImgUrl =
                    tAvatarImgUrlRepository.findFirstByCharacterIdOrderByCreatedAtDesc(characterId)?.avatarImgUrl
                val exp = tExpRepository.findFirstByCharacterIdOrderByCreatedAtDesc(characterId)
                val pop = tPopRepository.findFirstByCharacterIdOrderByCreatedAtDesc(characterId)?.pop
                val totalRank = tTotalRankRepository.findFirstByCharacterIdOrderByCreatedAtDesc(characterId)?.ranking
                val worldRank = tWorldRankRepository.findFirstByCharacterIdOrderByCreatedAtDesc(characterId)?.ranking

                return GetCharacterInfoByAccountIDResponse.generate(
                    character = character,
                    avatarImgURL = avatarImgUrl,
                    exp = exp,
                    totRank = totalRank,
                    worldRank = worldRank,
                    pop = pop
                )
            }
        }
        throw ViperException("does not exist")
    }
}
