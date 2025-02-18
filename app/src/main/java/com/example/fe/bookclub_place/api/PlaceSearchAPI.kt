package com.example.fe.bookclub_place.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlaceSearchAPI {
    // 나들이 북클럽 장소 검색
    @GET("api/places/search")
    fun searchPlaces(
        @Query("keyword") keyword: String,
        @Query("cursor") cursor: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<PlaceSearchResponse>

    // 나들이 북클럽 초기 세팅 - 현제 위치 기반 장소 리스트업
    @GET("api/places")
    fun sortPlaces(
        @Query("lat") lat: Double? = null, // 위도 - 현재 위치의 위도. 가까운순으로 조회할 때만 전달
        @Query("lon") lon: Double? = null, // 경도 - 현재 위치의 위도. 가까운순으로 조회할 때만 전달
        @Query("sort") sort: String = "all", //전체: all, 서점: bookstore, 북카페: cafe
        @Query("cursor") cursor: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<PlaceSearchResponse>

    // 공간 상세 조회 (placeId 기반)
    @GET("api/places/{placeId}")
    fun getPlaceDetails(
        @Path("placeId") placeId: Int
    ): Call<PlaceDetailResponse>

    // 공간 추천
    @GET("api/places/suggestions")
    fun getPlaceSuggestions(): Call<PlaceSuggestionResponse>

    // 공간 에디터 픽 조회
    @GET("api/places/editor-pick")
    fun getEditorPickPlaces(): Call<PlaceEditorPickResponse>
}
