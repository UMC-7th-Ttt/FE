package com.example.fe.search.api

import com.google.gson.annotations.SerializedName

data class BookSuggestionResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookSuggestionResult
)

data class BookSuggestionResult(
    @SerializedName("books") val books: List<BookResponse>
)
