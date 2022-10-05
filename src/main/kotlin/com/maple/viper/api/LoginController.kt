package com.maple.viper.api

import com.maple.viper.config.JwtTokenProvider
import com.maple.viper.dto.request.LoginRequest
import com.maple.viper.dto.request.UserRegistRequest
import com.maple.viper.dto.response.LoginResponse
import com.maple.viper.exception.InvalidRequestException
import com.maple.viper.service.LoginService
import com.maple.viper.service.RegistService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class LoginController(
    private val registService: RegistService,
    private val loginService: LoginService,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    /**
     * 최초 로그인. JWT Token 발행
     */
    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<LoginResponse> = with(loginService.loadUserByUsername(request.email)) {
        if (this == null) {
            throw InvalidRequestException("존재하지 않는 계정")
        } else if (!passwordEncoder.matches(request.password, this.password)) {
            throw InvalidRequestException("비밀번호 확인")
        } else {
            ResponseEntity.ok(
                LoginResponse(
                    jwtTokenProvider.createToken(
                        this.username,
                        listOf("ROLE_USER")
                    )
                )
            )
        }
    }

    @PostMapping("/regist")
    fun regist(
        @Valid @RequestBody request: UserRegistRequest
    ): ResponseEntity<Any> {
        val map = HashMap<String, String>()
        if (registService.insert(request)) {
            map["message"] = "회원가입/대표캐릭터 정보 저장 성공"
        } else {
            map["message"] = "회원가입을 완료하였으나, 대표캐릭터 정보를 불러오는 데에 실패"
        }
        return ResponseEntity.ok(map)
    }
}
