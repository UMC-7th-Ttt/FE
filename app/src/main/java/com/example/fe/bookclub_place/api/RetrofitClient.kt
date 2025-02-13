package com.example.fe.bookclub_place.api

import com.example.fe.search.api.BookSearchAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://3.38.209.11:8080/"

    // 서버 요청 시 자동으로 Authorization 헤더 추가
    private val authInterceptor = Interceptor { chain ->
        val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTczOTU4Njg3MSwiZW1haWwiOiJqb2hhZXVuMDgwMkBuYXZlci5jb20ifQ.efSXqmUIFV1ox3DqyedxQg3zRod_IrILAOAMu4LCsNzjcaARERl-sqa2A83jqpH3DIMtrvZrstl4VturnyzCeg"

        val request = chain.request().newBuilder()
            .addHeader("Authorization", token)  // 헤더에 자동으로 토큰 추가
            .build()
        chain.proceed(request)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)  // Interceptor 추가
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val placeApi: PlaceSearchAPI by lazy {
        retrofit.create(PlaceSearchAPI::class.java)
    }

    val bookApi: BookSearchAPI by lazy {
        retrofit.create(BookSearchAPI::class.java)
    }
}
