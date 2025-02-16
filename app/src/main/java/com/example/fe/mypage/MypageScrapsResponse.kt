package com.example.fe.mypage

import com.google.gson.annotations.SerializedName

data class MypageScrapsResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result
)

data class Result(
    @SerializedName("scraps") val scraps: List<Scrap>,
    @SerializedName("nextBookCursor") val nextBookCursor: Any?,
    @SerializedName("nextPlaceCursor") val nextPlaceCursor: Any?,
    @SerializedName("limit") val limit: Int,
    @SerializedName("hasNext") val hasNext: Boolean
)

data class Scrap(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("authorOrAddress") val authorOrAddress: String,
    @SerializedName("image") val image: String?,
    @SerializedName("type") val type: String,
    @SerializedName("createdAt") val createdAt: String
)