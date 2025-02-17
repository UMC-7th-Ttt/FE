package com.example.fe.search.api

data class BookResponse(
    val bookId: Int,
    val cover: String, // 책 표지 이미지 url
    val title: String,
    val author: String,
    val category: String,
    val publisher: String,
    val itemLink: String,
    var isScraped: Boolean
)
