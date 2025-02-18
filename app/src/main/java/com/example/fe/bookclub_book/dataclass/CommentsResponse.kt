package com.example.fe.bookclub_book.dataclass


data class CommentsResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val comments: List<Comment>,
        val commentCount: Int
    ) {
        data class Comment(
            val commentId: Int,
            val content: String,
            val writerId: Int,
            val writerNickname: String,
            val writerProfileImg: String,
            val createdAt: String,
            val isWriter: Boolean,
            var isLiked: Boolean,
            var likeCount: Int,
            val replyCount: Int,
            val parentId: Int,
            val replies: List<String>
        )
    }
}