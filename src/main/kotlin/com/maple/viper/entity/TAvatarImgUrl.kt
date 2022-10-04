package com.maple.viper.entity

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "t_avatar_img_url")
@IdClass(CompositeKey::class)
data class TAvatarImgUrl(
    @Id
    @Column(name = "character_id")
    val characterId: Long,
    @Column(name = "avatar_img_url")
    val avatarImgUrl: String,
    @Id
    @Column(name = "created_at")
    val createdAt: LocalDate
) {
    companion object {
        fun generateInsertModel(characterId: Long, avatarImgUrl: String) = TAvatarImgUrl(
            characterId = characterId,
            avatarImgUrl = avatarImgUrl,
            createdAt = LocalDate.now()
        )
    }
}
