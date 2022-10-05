package com.maple.viper.info

@Suppress("MagicNumber")
enum class ExpPortionInfo(
    val id: Int,
    val portion: String,
) {
    PORTION_210(210, "~209 성장의 비약"),
    PORTION_220(220, "~219 성장의 비약"),
    PORTION_230(230, "~229 성장의 비약"),
    PORTION_240(240, "~239 성장의 비약"),
    PORTION_EXTREME(250, "극한 성장의 비약")
}
