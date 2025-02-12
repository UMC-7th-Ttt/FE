package com.example.fe.login.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LoginRetrofitInterface {
    @POST("api/login")
    @Headers("Content-Type: application/json")  // 추가
    fun login(@Body request: LoginRequest): Call<LoginResponse>
}