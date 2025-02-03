package com.example.fe.signup

import com.google.gson.annotations.SerializedName

data class MemberResult(
    @SerializedName(value = "id") val id: Int, // memberId
    @SerializedName(value = "nickname") val nickname: String,
    @SerializedName(value = "profileUrl") val profileUrl: String
)

data class AuthResponse (
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName("result") val result: Any?
)