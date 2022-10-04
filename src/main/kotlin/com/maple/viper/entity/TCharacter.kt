package com.maple.viper.entity

import com.maple.viper.dto.response.GetCharacterInfoByAccountIDResponse
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_character")
data class TCharacter(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column(name = "user_id")
    val userId: Long,
    @Column(name = "world_name", nullable = false)
    val worldName: String,
    @Column(nullable = false)
    val name: String,
    @Column(name = "created_at")
    val createdAt: LocalDate
) {
    companion object {
        fun generateInsertModel(userId: Long, data: GetCharacterInfoByAccountIDResponse) = TCharacter(
            userId = userId,
            worldName = data.worldName,
            name = data.characterName,
            createdAt = LocalDate.now()
        )
    }
}
