package com.example.fe.signup.service.email

import com.google.gson.annotations.SerializedName

data class AuthCodeRequestRequest (
    @SerializedName(value = "email") var email: String
)