package com.maple.viper.info

@Suppress("MagicNumber")
enum class JobClassificationInfo(
    val id: Int,
    val value: String
) {
    WARRIOR(0, "전사"),
    WIZARD(1, "마법사"),
    ARCHER(2, "궁수"),
    THIEF(3, "도적"),
    PIRATE(4, "해적");
}
