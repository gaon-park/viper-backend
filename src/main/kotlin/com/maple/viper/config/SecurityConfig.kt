package com.maple.viper.config

import com.maple.viper.service.LoginService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
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
    private val loginService: LoginService,
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .httpBasic()
                // todo 프론트엔드 서버 분리 후 주석 제거
//            .and()
//            .authorizeRequests() // 요청에 대한 권한 체크
//            .antMatchers("/user", "/user/**").hasRole("USER")
//            .anyRequest().permitAll() // 그 외 나머지 요청은 누구나 접근 가능
            .and()
            .addFilterBefore(JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter::class.java)

        // 로그인
        http
            .formLogin()
            .loginPage("/login")

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

    /**
     * DB 사용자 정보를 확인
     */
    @Autowired
    fun configure(auth: AuthenticationManagerBuilder) {
        // todo session 저장 로그인 상태 확인 로직
        auth.userDetailsService(loginService)
    }
}
