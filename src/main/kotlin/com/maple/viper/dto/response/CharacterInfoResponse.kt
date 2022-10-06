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
@Suppress("LongParameterList")
data class CharacterInfoResponse(
    @JsonProperty("avatar_img_url")
    val avatarImgURL: String,
    @JsonProperty("world_name")
    val worldName: String,
    @JsonProperty("character_name")
    val characterName: String,
    @JsonProperty("lev")
    val lev: Int,
    @JsonProperty("exp")
    val exp: Long,
    @JsonProperty("job")
    val job: String,
    @JsonProperty("job_detail")
    val jobDetail: String,
    @JsonProperty("pop")
    val pop: Int,
    @JsonProperty("total_rank")
    val totRank: Long,
    @JsonProperty("world_rank")
    val worldRank: Long,
) {
    companion object {
        fun generate(
            character: TCharacter,
            avatarImgURL: String?,
            exp: TExp?,
            totRank: Long?,
            worldRank: Long?,
            pop: Int?) =
            CharacterInfoResponse(
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
