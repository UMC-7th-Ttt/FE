package com.example.fe.login.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRetrofitInterface {
    @POST("/api/login")  // HTTP 메서드와 API 주소
    fun login(@Body user: LoginRequest): Call<LoginResponse>
}