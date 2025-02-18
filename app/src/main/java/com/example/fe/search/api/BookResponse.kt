package com.example.fe.search.api

import com.google.gson.annotations.SerializedName

data class BookResponse(
    @SerializedName("id")val bookId: Int,
    val cover: String, // 책 표지 이미지 url
    val title: String,
    val author: String,
    val category: String,
    val publisher: String,
    val itemLink: String,
    var isScraped: Boolean
)
