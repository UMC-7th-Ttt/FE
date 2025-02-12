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
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTczODg1MDkyMiwiZW1haWwiOiJqdW55MjAwQG5hdmVyLmNvbSJ9.FadyRE3VgpsBwK0mq06cq-R89gAFGrYEqsEV9-wYIk6xQdBLbik1tAA5EaoQGroO7zFUblSLBTe2amhd4362Mw") // 🔹토큰
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
