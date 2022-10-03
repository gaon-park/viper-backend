package com.maple.viper.form

import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotEmpty

data class UserRegistForm(

    @field:NotEmpty(message = "이메일을 입력해주세요.")
    var email: String = "",

    @field:NotEmpty(message = "비밀번호를 입력해주세요.")
    var password: String = "",

    @field:NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    var confirmPassword: String = "",

    @field:AssertTrue(message = "이용약관을 읽고 동의버튼을 눌러주세요.")
    var terms: Boolean = false,
) {
    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    fun isPasswordConfirm(): Boolean = (password == confirmPassword)
}
