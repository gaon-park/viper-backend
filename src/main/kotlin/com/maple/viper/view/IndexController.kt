package com.maple.viper.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

/**
 * todo remove
 * spring security 프론트 테스트용 controller
 */
@Controller
@Suppress("FunctionOnlyReturningConstant")
class IndexController {
    @GetMapping
    fun index(): String {
        return "login"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @PostMapping("/login/auth")
    fun loginAuth(): String {
        return "redirect:/user/setting"
    }
}
