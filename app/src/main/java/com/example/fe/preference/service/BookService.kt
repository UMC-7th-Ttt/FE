package com.example.fe.preference.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface BookService {
    @GET("/api/books/bestsellers")
    fun getBooks(
        @Header("Authorization") token: String
    ): Call<BookResponse>
}