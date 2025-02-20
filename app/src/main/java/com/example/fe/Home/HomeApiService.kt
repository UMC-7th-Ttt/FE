package com.example.fe.Home

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HomeApiService {
    @GET("/api/home/")  // ✅ 엔드포인트 확인 필요
    fun getHomeData(): Call<HomeResponse>
}