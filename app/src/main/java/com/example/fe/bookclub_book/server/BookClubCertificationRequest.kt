package com.example.fe.bookclub_book.server

data class BookClubCertificationRequest(
    val title: String,
    val content: String,
    val currentPage: Int,
    val imgUrl: String,
    val isSecret: Boolean
)