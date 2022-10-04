package com.maple.viper.entity

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "t_world_rank")
@IdClass(CompositeKey::class)
data class TWorldRank(
    @Id
    @Column(name = "character_id")
    val characterId: Long,
    @Column(name = "ranking")
    val ranking: Long,
    @Id
    @Column(name = "created_at")
    val createdAt: LocalDate
) {
    companion object {
        fun generateInsertModel(characterId: Long, ranking: Long) = TWorldRank(
            characterId = characterId,
            ranking = ranking,
            createdAt = LocalDate.now()
        )
    }
}
