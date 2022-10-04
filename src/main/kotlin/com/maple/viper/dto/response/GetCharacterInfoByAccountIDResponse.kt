package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
import com.maple.viper.entity.TCharacter
import com.maple.viper.entity.TExp
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.NoArgsConstructor

@Builder
@NoArgsConstructor
@AllArgsConstructor
data class GetCharacterInfoByAccountIDResponse(
    @JsonProperty("AvatarImgURL")
    val avatarImgURL: String,
    @JsonProperty("WorldName")
    val worldName: String,
    @JsonProperty("CharacterName")
    val characterName: String,
    @JsonProperty("Lev")
    val lev: Int,
    @JsonProperty("Exp")
    val exp: Long,
    @JsonProperty("Job")
    val job: String,
    @JsonProperty("JobDetail")
    val jobDetail: String,
    @JsonProperty("Pop")
    val pop: Int,
    @JsonProperty("TotRank")
    val totRank: Long,
    @JsonProperty("WorldRank")
    val worldRank: Long,
) {
    companion object {
        fun generate(character: TCharacter, avatarImgURL: String?, exp: TExp?, totRank: Long?, worldRank: Long?, pop: Int?) =
            GetCharacterInfoByAccountIDResponse(
                avatarImgURL = avatarImgURL ?: "",
                worldName = character.worldName,
                characterName = character.name,
                lev = exp?.lev ?: 0,
                exp = exp?.exp ?: 0,
                job = character.job,
                jobDetail = character.jobDetail,
                pop = pop ?: 0,
                totRank = totRank ?: 0,
                worldRank = worldRank ?: 0
            )
    }
}
