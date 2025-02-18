package com.example.fe.mypage

import com.example.fe.bookclub_book.server.AuthInterceptor
import com.example.fe.bookclub_book.server.BookClubRetrofitInterface
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

val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDMxMTY3MywiZW1haWwiOiJhZG1pbjJAbmF2ZXIuY29tIn0.JwzCFHzkGRW-CESnhvcFUG6gc55MH1q10uEHvp12qubguOuKZXsQZyVrAY2mADTmwWDecC9tC5reXLh6tUR-kg"
// API 인터페이스 인스턴스 생성
val api: MyPageRetrofitInterface by lazy {
    getRetrofit(authToken).create(MyPageRetrofitInterface::class.java)
}