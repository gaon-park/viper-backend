package com.maple.viper.view

import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.maple.viper.exception.AlreadyExistException
import com.maple.viper.form.UserRegistForm
import com.maple.viper.service.TUserService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.mvc.support.RedirectAttributes
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
        model.addAttribute("form", getSessionForm(model))
        return "regist"
    }

    @Suppress("SwallowedException")
    @PostMapping("/regist")
    fun regist(
        @Valid form: UserRegistForm, bindingResult: BindingResult, redirectAttributes: RedirectAttributes
    ): String {
        return when {
            bindingResult.hasErrors() -> {
                redirectAttributes.addFlashAttribute(
                    "error",
                    bindingResult.fieldErrors.map { it.defaultMessage }.joinToString { "<br>" })
                redirectAttributes.addFlashAttribute(
                    "form",
                    jsonMapper().writeValueAsString(form)
                )
                "redirect:/regist"
            }
            else -> {
                try {
                    tUserService.insert(form)
                    "redirect:/"
                } catch (e: AlreadyExistException) {
                    redirectAttributes.addFlashAttribute("error", "이미 등록되어있는 이메일입니다.")
                    redirectAttributes.addFlashAttribute(
                        "form",
                        jsonMapper().writeValueAsString(form)
                    )
                    "redirect:/regist"
                }
            }
        }
    }

    fun getSessionForm(model: Model): UserRegistForm {
        val form = model.getAttribute("form")
        return if (form != null) jsonMapper().readValue(form.toString(), UserRegistForm::class.java)
        else UserRegistForm()
    }
}
