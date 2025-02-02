package com.example.fe

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val client = OkHttpClient.Builder()
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://3.38.209.11:8080") // HTTP인지 확인
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) // 클라이언트 설정 추가
            .build()
    }

    val api: PlaceSearchService by lazy {
        retrofit.create(PlaceSearchService::class.java)
    }
}



//object RetrofitInstance {
//    private const val BASE_URL = "http://3.38.209.11:8080/" // 실제 API 주소로 변경해야 함
//
//    val api: PlaceSearchService by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create()) // JSON 변환기 추가
//            .build()
//            .create(PlaceSearchService::class.java)
//    }
//}
