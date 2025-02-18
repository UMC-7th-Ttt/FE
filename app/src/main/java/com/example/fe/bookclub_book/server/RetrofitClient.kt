package com.example.fe.bookclub_book.server

import com.example.fe.mypage.MyPageRetrofitInterface
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://3.38.209.11:8080"

// 새로운 토큰 설정
val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDQ3MjcxNywiZW1haWwiOiJqdW55MjAwQG5hdmVyLmNvbSJ9.Ix51rD7DP5EOySibSxPIS1gHn9FwjDROAx5OaeWAjtK2SrNTJG-KnmzYs8v3QfTdGUpT3BaQezdYmkllWHEScQ"

// Retrofit 인스턴스 생성
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

// API 인터페이스 인스턴스 생성
val api: BookClubRetrofitInterface by lazy {
    getRetrofit(authToken).create(BookClubRetrofitInterface::class.java)
}

val api2: MyPageRetrofitInterface by lazy {
    getRetrofit(authToken).create(MyPageRetrofitInterface::class.java)
}
