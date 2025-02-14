package com.example.fe.BookLetter

import com.google.gson.annotations.SerializedName

data class BookLetterResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookLetterDetail
)

data class BookLetterDetail(
    @SerializedName("title") val title: String,
    @SerializedName("subtitle") val subtitle: String,
    @SerializedName("editor") val editor: String,
    @SerializedName("isWriter") val isWriter: Boolean,
    @SerializedName("content") val content: String,
    @SerializedName("cover_img") val coverImg: String,
    @SerializedName("books") val books: List<BookDetail>
)

data class BookDetail(
    @SerializedName("bookId") val bookId: Long,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("cover") val cover: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("itemPage") val itemPage: Int,
    @SerializedName("categoryName") val categoryName: String,
    @SerializedName("hasEbook") val hasEbook: Boolean,
    @SerializedName("description") val description: String
)
