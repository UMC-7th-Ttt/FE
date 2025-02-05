package com.example.fe.login.service

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName("result") val result: Any?
)