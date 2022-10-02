package com.maple.viper.view

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
// todo remove
@Suppress("FunctionOnlyReturningConstant")
class UserController {
    @GetMapping("/user/setting")
    fun index(): String {
        return "/user/setting"
    }
}
