package com.example.fe.bookclub_book.server

import com.example.fe.mypage.MyPageRetrofitInterface
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

val authToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDI5OTA5MCwiZW1haWwiOiJtb2Rlc3RuYXR1cmVAbmF2ZXIuY29tIn0.GdsOYoY9QlaHaqtdITnCa9OXLt2OeVYRqgNSOFVzD7SK3wjDtDJOrkZSaoLay6RKc6Tf4EIxo_dZZcZs14BTeQ"
// API 인터페이스 인스턴스 생성
val api: BookClubRetrofitInterface by lazy {
    getRetrofit(authToken).create(BookClubRetrofitInterface::class.java)
}

val api2: MyPageRetrofitInterface by lazy {
    getRetrofit(authToken).create(MyPageRetrofitInterface::class.java)
}