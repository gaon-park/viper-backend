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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val loginService: LoginService
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/user", "/user/**")
            .authenticated()

        // 로그인
        http
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/login/auth")
            .failureUrl("/login?error=true")
            .defaultSuccessUrl("/", true)
            .usernameParameter("login-email")
            .passwordParameter("login-password")

        // 로그아웃
        http
            .logout()
            .logoutRequestMatcher(AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/")
            .invalidateHttpSession(true)

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
