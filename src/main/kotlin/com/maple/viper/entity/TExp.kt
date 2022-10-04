package com.maple.viper.entity

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.Table

@Entity
@Table(name = "t_exp")
@IdClass(CompositeKey::class)
data class TExp(
    @Id
    @Column(name = "character_id")
    val characterId: Long,
    @Column(name = "lev")
    val lev: Int,
    @Column(name = "exp")
    val exp: Long,
    @Id
    @Column(name = "created_at")
    val createdAt: LocalDate
) {
    companion object {
        fun generateInsertModel(characterId: Long, lev: Int, exp: Long) = TExp(
            characterId = characterId,
            lev = lev,
            exp = exp,
            createdAt = LocalDate.now()
        )
    }
}
