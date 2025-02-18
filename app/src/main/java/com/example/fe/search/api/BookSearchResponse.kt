package com.example.fe.search.api

import com.google.gson.annotations.SerializedName

data class BookSearchResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookSearchResult
)

data class BookSearchResult(
    @SerializedName("books") val books: List<BookResponse>,
    @SerializedName("nextCursor") val nextCursor: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("hasNext") val hasNext: Boolean
)
