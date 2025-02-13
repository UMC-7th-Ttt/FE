package com.example.fe.search.api

import com.google.gson.annotations.SerializedName

data class BookUserSuggestionResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookUserSuggestionResult
)

data class BookUserSuggestionResult(
    @SerializedName("memberNickname") val memberNickname: String,
    @SerializedName("books") val books: List<BookResponse>
)
