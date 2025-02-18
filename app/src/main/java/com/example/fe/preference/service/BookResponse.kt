package com.example.fe.preference.service

import androidx.core.app.NotificationCompat.MessagingStyle.Message


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
    val id: Int,
    val cover: String,
    val title: String,
    val author: String,
    val category: String,
    val publisher: String,
    val mainSentences: String
)
