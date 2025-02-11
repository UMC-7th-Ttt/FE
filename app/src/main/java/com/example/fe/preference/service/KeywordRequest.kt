package com.example.fe.preference.service

import com.google.gson.annotations.SerializedName

data class KeywordRequest (
    @SerializedName("preferCategory1") var preferCategory1: List<String>,
    @SerializedName("preferCategory2") var preferCategory2: List<String>,
    @SerializedName("preferCategory2") var preferCategory3: List<String>,
    @SerializedName("preferBookId") var preferBookId: Int
)