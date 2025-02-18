package com.example.fe.search.api

import com.google.gson.annotations.SerializedName

data class BookEditorPickResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookEditorPickResult
)

data class BookEditorPickResult(
    @SerializedName("bookLetterTitle") val bookLetterTitle: String, // 북레터 제목
    @SerializedName("books") val books: List<BookResponse>
)
