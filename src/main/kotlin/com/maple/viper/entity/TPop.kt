package com.maple.viper.entity

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "t_pop")
@IdClass(CompositeKey::class)
data class TPop(
    @Id
    @Column(name = "character_id")
    val characterId: Long,
    @Column(name = "pop")
    val pop: Int,
    @Id
    @Column(name = "created_at")
    val createdAt: LocalDate
) {
    companion object {
        fun generateInsertModel(characterId: Long, pop: Int) = TPop(
            characterId = characterId,
            pop = pop,
            createdAt = LocalDate.now()
        )
    }
}
