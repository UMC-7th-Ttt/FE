package com.example.fe.login.service

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName("result") val result: LoginResult?
)

data class LoginResult(
    val createdAt: String,
    val id: Int,
    val nickname: String,
    val profileUrl: String,
    val role: String,
    val accessToken: String
)