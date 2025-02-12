package com.example.fe.preference.service

data class BookResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: BookResult
)

data class BookResult(
    val books: List<Book>
)

data class Book(
    val id: Long,
    val cover: String,
    val title: String,
    val author: String,
    val category: String,
    val mainSentences: String
)