package com.example.fe

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PlaceSearchService {
    @GET("/api/places/search")
    fun searchPlaces(
        @Query("keyword") keyword: String,
        @Query("cursor") cursor: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<PlaceSearchResponse>
}
