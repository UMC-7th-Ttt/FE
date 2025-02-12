package com.example.fe.Home

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApiService {
    @GET("/api/home/")  // ✅ 엔드포인트 확인 필요
    fun getHomeData(
        @Header("Authorization") token: String // 동적으로 토큰 전달 가능
    ): Call<HomeResponse>
}