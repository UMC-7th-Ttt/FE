package com.example.fe.Home

import com.google.gson.annotations.SerializedName

data class BannerItem(
    @SerializedName("bookLetterId") val bookLetterId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("subTitle") val subtitle: String,
    @SerializedName("editor") val author: String,
    @SerializedName("coverImg") val coverImg: String // API에서 URL 형태로 제공
)
