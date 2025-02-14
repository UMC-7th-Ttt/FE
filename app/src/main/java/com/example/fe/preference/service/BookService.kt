package com.example.fe.preference.service

import com.example.fe.login.service.LoginRequest
import com.example.fe.login.service.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface BookService {
    @GET("/api/books/bestsellers")
    fun getBestSellers(
        @Header("Authorization") authHeader: String
    ): Call<BookResponse>
}
