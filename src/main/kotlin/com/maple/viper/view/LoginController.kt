package com.maple.viper.view

import com.maple.viper.form.UserRegistForm
import com.maple.viper.service.TUserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
@Suppress("FunctionOnlyReturningConstant")
class LoginController(
    private val tUserService: TUserService
) {

    @GetMapping("/login")
    fun showLogin(): String {
        return "login"
    }

    @PostMapping("/login/auth")
    fun loginAuth(): String {
        return "redirect:/"
    }

    @GetMapping("/regist")
    fun showRegist(model: Model): String {
        model.addAttribute("form", UserRegistForm())
        return "regist"
    }

    @PostMapping("/regist")
    fun regist(
        @Valid form: UserRegistForm,
        bindingResult: BindingResult,
    ): String {
        if (bindingResult.hasErrors()) {
            return "redirect:/regist"
        }
        // todo 중복확인
        tUserService.insert(form)
        return "redirect:/"
    }
}
