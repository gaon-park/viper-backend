package com.maple.viper.exception

open class InvalidRequestException(private val msg: String) : Exception(msg)
