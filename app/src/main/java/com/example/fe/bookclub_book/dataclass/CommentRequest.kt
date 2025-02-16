package com.example.fe.bookclub_book.dataclass

data class CommentRequest(
    val content: String,
    val parentCommentId: Int? = null
)