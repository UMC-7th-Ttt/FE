package com.example.fe.bookclub_book.server

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://3.38.209.11:8080"

fun getRetrofit(token: String): Retrofit {
    val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(token)) // 인터셉터 추가
        .build()

    val gson = GsonBuilder()
        .setLenient()
        .create()

    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client) // OkHttpClient 설정
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
}

val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTczOTc2ODE2MiwiZW1haWwiOiJtb2Rlc3RuYXR1cmVAbmF2ZXIuY29tIn0.rGfiXRBkJ1x1mpFl5I1LDBClf_TddLfj0e0l_YfgtWU-DqEQ83yXNdicdBmz9k8tmAyqK5iNHAafTdvKo8RIsg"
// API 인터페이스 인스턴스 생성
val api: BookClubRetrofitInterface by lazy {
    getRetrofit(authToken).create(BookClubRetrofitInterface::class.java)
}