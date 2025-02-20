package com.example.fe.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    private const val BASE_URL = "http://3.38.209.11:8080"

    //
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request: Request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDYxNjg2OCwiZW1haWwiOiJhZG1pbjJAbmF2ZXIuY29tIn0.V9bS0xnzzrO9LlBKqHSoHEh0s2whSXMgp9nJNha1UT6S81xAJRsl_GQpz15T_P89Rt9c9-InfWcw-koGcOh1tg") // 🔹토큰
                .build()
            chain.proceed(request)
        }
        .build()

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // ✅ OkHttpClient 추가
            .build()
    }
}
