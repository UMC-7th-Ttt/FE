package com.example.fe.BookDetail

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BookDetailService {
    @GET("/api/books/{bookId}")
    fun getBookDetail(
        @Header("Authorization") token: String,
        @Path("bookId") bookId: Long // 📌 bookId를 경로 변수로 전달
    ): Call<BookDetailResponse>
}