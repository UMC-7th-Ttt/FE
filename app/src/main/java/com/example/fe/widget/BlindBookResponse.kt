package com.example.fe.widget

data class BlindBookResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
) {
    data class Result(
        val id: Int,
        val title: String,
        val mainSentences: String
    )
}