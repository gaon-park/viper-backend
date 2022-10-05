package com.maple.viper.exception

class AlreadyExistException(private val msg: String) : InvalidRequestException(msg)
