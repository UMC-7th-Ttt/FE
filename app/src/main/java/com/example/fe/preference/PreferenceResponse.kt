package com.example.fe.preference

import com.google.gson.annotations.SerializedName

data class PreferenceResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String
)