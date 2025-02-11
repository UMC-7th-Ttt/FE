package com.example.fe.bookclub_book.server

data class CommentRequest(
    val content: String,
    val parentCommentId: Int? = null
)