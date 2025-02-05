package com.example.fe.signup.service

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName(value = "email") var email: String,
    @SerializedName(value = "password")var password: String
)

data class Nickname (
    @SerializedName(value = "nickname") var nickname: String
)