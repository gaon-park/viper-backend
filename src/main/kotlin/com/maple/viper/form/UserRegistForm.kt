package com.maple.viper.form

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.validation.constraints.AssertTrue
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UserRegistForm(

    @field:NotNull(message = "메이플 계정 ID를 입력해주세요.")
    var accountId: Int? = null,

    @field:NotEmpty(message = "이메일을 입력해주세요.")
    var email: String = "",

    @field:NotEmpty(message = "비밀번호를 입력해주세요.")
    var password: String = "",

    @field:NotEmpty(message = "비밀번호 확인을 입력해주세요.")
    var confirmPassword: String = "",

    @field:AssertTrue(message = "이용약관을 읽고 동의버튼을 눌러주세요.")
    var terms: Boolean = false,
) {

    @JsonIgnore
    @AssertTrue(message = "비밀번호가 일치하지 않습니다.")
    fun isPasswordValidate(): Boolean = (password == confirmPassword)
}
