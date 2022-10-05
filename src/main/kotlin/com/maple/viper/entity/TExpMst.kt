package com.maple.viper.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "t_exp_mst")
data class TExpMst(
    @Id
    @Column(name = "target_lev")
    val targetLev: Int,
    @Column(name = "exp")
    val exp: Long,
    @Column(name = "rate_of_increase")
    val rateOfIncrease: Double,
)
