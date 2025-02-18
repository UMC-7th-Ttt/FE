package com.example.fe.mypage

import com.example.fe.bookclub_book.dataclass.CalendarResponse
import com.example.fe.mypage.adapter.CalendarRVAdapter
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

data class ScrapDeleteRequest(
    val scraps: List<ScrapDeleteItem>
)

data class ScrapDeleteItem(
    val scrapId: Int,
    val type: String
)

interface MyPageRetrofitInterface {
    //스크랩 폴더 목록 가져오기
    @GET("/api/scraps/folders")
    fun getFolders(): Call<ScrapFolderResponse>

    //스크랩 목록 가져오기
    @GET("/api/scraps/folders/{folderId}")
    fun getScraps(
        @Path("folderId") folderId: Int,
        @Query("bookCursor") bookCursor: Long,
        @Query("placeCursor") placeCursor: Long,
        @Query("limit") limit: Int
    ): Call<MypageScrapsResponse>

    //새 폴더 만들기
    @POST("api/scraps/folders")
    fun createFolder(
        @Query("folder") folder: String
    ): Call<CreateFolderResponse>

    //스크랩 삭제하기
    @HTTP(method = "DELETE", path = "/api/scraps/folders/{folderId}/remove", hasBody = true)
    fun deleteScraps(
        @Path("folderId") folderId: Int,
        @Body requestBody: ScrapDeleteRequest
    ): Call<MypageScrapsResponse>

    //캘린더에 리뷰 가져오기
    @GET("/api/reviews/calendar")
    fun getCalendarReviews(
        @Query("year") year: Int,
        @Query("month") month: Int
    ): Call<CalendarResponse>

    //스크랩 이동하기
    @PATCH("/api/scraps/folders/{folderId}")
    fun moveScraps(
        @Path("folderId") folderId: Long,
        @Body request: MoveScrapsRequest
    ): Call<MoveScrapsResponse>

    @DELETE("/api/scraps/folders/{folderId}")
    fun deleteFolder(@Path("folderId") folderId: Int): Call<DeleteFolderResponse>

    @GET("/api/users")
    fun getUser(): Call<UserResponse>

    //서평 모아보기
    @GET("/api/reviews/")
    fun getReviews(
        @Query("cursor") cursor: Int,
        @Query("limit") limit: Int
    ): Call<ReviewListResponse>

}