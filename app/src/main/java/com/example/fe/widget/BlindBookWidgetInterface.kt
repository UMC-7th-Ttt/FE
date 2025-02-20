package com.example.fe.widget

import com.example.fe.bookclub_book.server.BookClubUserResponse
import retrofit2.Call
import retrofit2.http.GET

interface BlindBookWidgetInterface {
    //유저 정보 조회
    @GET("/api/books/book-quotes-suggestions")
    fun getQuotes(): Call<BlindBookResponse>
}
