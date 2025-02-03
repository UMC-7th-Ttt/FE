package com.example.fe.signup

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName(value = "email") var email: String,
    @SerializedName(value = "password")var password: String,
    @SerializedName(value = "nickname")var nickname: String,
    @SerializedName(value = "profileUrl")var profileUrl: String
)

data class Nickname (
    @SerializedName(value = "nickname") var nickname: String
)