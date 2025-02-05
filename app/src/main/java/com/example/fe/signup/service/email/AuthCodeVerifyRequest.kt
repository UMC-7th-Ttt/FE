package com.example.fe.signup.service.email

import com.google.gson.annotations.SerializedName

data class AuthCodeVerifyRequest (
    @SerializedName(value = "email") var email: String,
    @SerializedName(value = "code") var code: String
)