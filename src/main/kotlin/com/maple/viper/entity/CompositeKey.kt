package com.maple.viper.entity

import java.time.LocalDate

data class CompositeKey(
    var characterId: Long = 0,
    var createdAt: LocalDate = LocalDate.now()
): java.io.Serializable

