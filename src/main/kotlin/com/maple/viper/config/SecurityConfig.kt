package com.maple.viper.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .httpBasic()
            .and()
            .authorizeRequests() // 요청에 대한 권한 체크
            .antMatchers("/user", "/user/**").hasRole("USER")
            // todo admin 로직 생성 후 주석 제거
//            .antMatchers("/batch", "/batch/**").hasRole("ADMIN")
            .anyRequest().permitAll() // 그 외 나머지 요청은 누구나 접근 가능
            .and()
            .addFilterBefore(
                JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun authenticationManager(auth: AuthenticationConfiguration): AuthenticationManager {
        return auth.authenticationManager
    }

    /**
     * 패스워드 인코딩 클래스 등록
     * 사용자가 입력한 패스워드가 DB에 저장된 값과 동일한지 판단
     */
    @Bean
    fun getPasswordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
