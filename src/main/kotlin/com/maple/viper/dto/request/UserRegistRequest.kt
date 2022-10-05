package com.maple.viper.dto.request

import javax.validation.constraints.NotEmpty

data class UserRegistRequest(
    @NotEmpty(message = "메이플 계정 ID는 필수 항목입니다.")
    val accountId: Int,

    @NotEmpty(message = "이메일은 필수 항목입니다.")
    val email: String,

    @NotEmpty(message = "비밀번호는 필수 항목입니다.")
    val password: String,

    @NotEmpty(message = "비밀번호 확인은 필수 항목입니다.")
    val confirmPassword: String,

    @NotEmpty(message = "이용약관 동의는 필수 항목입니다.")
    val terms: Boolean
)
