package com.maple.viper.dto.response

import com.fasterxml.jackson.annotation.JsonProperty
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
)
