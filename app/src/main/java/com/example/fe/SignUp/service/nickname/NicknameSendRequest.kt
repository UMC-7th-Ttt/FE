package com.example.fe.SignUp.service.nickname

import com.google.gson.annotations.SerializedName

data class NicknameSendRequest (
    @SerializedName(value = "memberId") var memberId: Int,
    @SerializedName(value = "nickname") var nickname: String
)