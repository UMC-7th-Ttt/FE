package com.example.fe.signup.service

import com.google.gson.annotations.SerializedName

data class NicknameSendResponse (
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName("result") val result: NicknameResultData?
)

data class NicknameResultData(
    val createdAt: String,
    val id: Int,
    val nickname: String,
    val profileUrl: String,
    val role: String
)