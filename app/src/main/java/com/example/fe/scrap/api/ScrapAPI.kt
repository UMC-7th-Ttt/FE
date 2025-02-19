package com.example.fe.scrap.api

import com.example.fe.mypage.ScrapFolderResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScrapAPI {
    // 스크랩 폴더 목록
    @GET("api/scraps/folders")
    fun getScrapFolders(): Call<ScrapFolderResponse>

    // 도서 스크랩
    @POST("api/books/{bookId}/scraps")
    fun scrapBook(
        @Path("bookId") bookId: Long,
        @Query("folder") folder: String
    ): Call<Void>

    // 공간 스크랩
    @POST("api/places/{placeId}/scraps")
    fun scrapPlace(
        @Path("placeId") placeId: Int,
        @Query("folder") folder: String
    ): Call<Void>

    // 도서 스크랩 취소
    @DELETE("api/books/{bookId}/scraps")
    fun deleteBookScrap(@Path("bookId") bookId: Long): Call<Void>

    // 공간 스크랩 취소
    @DELETE("api/places/{placeId}/scraps")
    fun deletePlaceScrap(@Path("placeId") placeId: Int): Call<Void>

}
