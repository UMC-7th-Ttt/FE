package com.example.fe.bookclub_book.server

import com.example.fe.bookclub_book.dataclass.BookClubBookJoin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookClubRetrofitInterface {
    @GET("api/book-clubs/{bookClubId}/join")
    fun getBookClubInfo(@Path("bookClubId") bookClubId: Int): Call<BookClubJoinInfoResponse>
    @POST("api/book-clubs/{bookClubId}/join")
    fun joinBookClub(@Body bookClubBookJoin: BookClubBookJoin): Call<Void>

    @GET("api/book-clubs/{bookClubId}/details")
    fun getBookClubDetail(@Path("bookClubId") bookClubId: Int): Call<BookClubDetailResponse>

    @GET("/api/reading-records/{readingRecordId}")
    fun getBookClubReviewDetail(@Path("readingRecordId") readingRecordId: Int): Call<BookClubReviewDetailResponse>

    @GET("api/book-clubs/")
    fun getBookClubMonth(): Call<BookClubMonthResponse>

    @GET("api/reading-records")
    fun getReadingRecords(): Call<ReadingRecordsListResponse>
}