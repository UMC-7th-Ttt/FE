package com.example.fe.BookLetter

import com.google.gson.annotations.SerializedName

data class BookLetterResponse(
     val isSuccess: Boolean,
     val code: String,
     val message: String,
    val result: BookLetterDetail
)

data class BookLetterDetail(
     val title: String,
     val subtitle: String,
    val editor: String,
     val isWriter: Boolean,
     val content: String,
 val coverImg: String,
    @SerializedName("bookList") val books: List<BookDetail>
)

data class BookDetail(
     val bookId: Long,
     val title: String,
     val author: String,
     val cover: String,
     val publisher: String,
    val itemPage: Int,
     val categoryName: String,
    val hasEbook: Boolean,
     val description: String
)
