package com.maple.viper.entity

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "t_total_rank")
@IdClass(CompositeKey::class)
data class TTotalRank(
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
        fun generateInsertModel(characterId: Long, ranking: Long) = TTotalRank(
            characterId = characterId,
            ranking = ranking,
            createdAt = LocalDate.now()
        )
    }
}
