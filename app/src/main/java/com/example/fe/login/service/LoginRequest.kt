package com.example.fe.login.service

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName(value = "email") var email: String,  // 사용자의 이메일
    @SerializedName(value = "password") var password: String,  // 사용자의 비밀번호
)