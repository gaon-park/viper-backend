package com.maple.viper.advice

import com.maple.viper.exception.ErrorMessage
import com.maple.viper.exception.InvalidRequestException
import com.maple.viper.exception.ViperException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionControllerAdvice {

    @ExceptionHandler
    fun handle1(ex: InvalidRequestException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(
                ex.javaClass.simpleName,
                HttpStatus.BAD_REQUEST.value(),
                ex.message
            ), HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler
    fun handler2(ex: ViperException): ResponseEntity<ErrorMessage> =
        ResponseEntity(
            ErrorMessage(
                ex.javaClass.simpleName,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.message
            ), HttpStatus.INTERNAL_SERVER_ERROR
        )
}
