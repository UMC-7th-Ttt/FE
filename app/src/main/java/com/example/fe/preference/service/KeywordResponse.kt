package com.example.fe.preference.service

import com.google.gson.annotations.SerializedName

data class KeywordResponse (
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
    @SerializedName(value = "code") val code: String,
    @SerializedName(value = "message") val message: String,
    @SerializedName("result") val result: Any?
)