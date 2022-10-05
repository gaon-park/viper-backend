package com.maple.viper.exception

data class ErrorMessage(
    var exception: String,
    var status: Int? = null,
    var message: String? = null
)
