package com.example.fe.bookclub_book.server

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookClubRetrofitInterface {
    //유저 정보 조회
    @GET("/api/book-clubs/home")
    fun getUser(): Call<BookClubUserResponse>

    //북클럽 가입하기
    @GET("api/book-clubs/{bookClubId}/join")
    fun getBookClubInfo(@Path("bookClubId") bookClubId: Int): Call<BookClubJoinInfoResponse>

    @POST("api/book-clubs/{bookClubId}/join")
    fun joinBookClub(
        @Path("bookClubId") bookClubId: Long
    ): Call<BookClubJoinResponse>

    //북클럽 상세 조회
    @GET("api/book-clubs/{bookClubId}/details")
    fun getBookClubDetail(@Path("bookClubId") bookClubId: Int): Call<BookClubDetailResponse>

    //멤버 리뷰 목록 조회
    @GET("api/reading-records")
    fun getReadingRecords(): Call<ReadingRecordsListResponse>

    //멤버 리뷰 상세 조회
    @GET("/api/reading-records/{readingRecordId}")
    fun getBookClubReviewDetail(@Path("readingRecordId") readingRecordId: Int): Call<BookClubReviewDetailResponse>

    @GET("api/book-clubs/home/bookClubs")
    fun getBookClubByMonth(): Call<BookClubByMonthResponse>

    //서평 댓글 기능
    @GET("/api/reading-records/{readingRecordId}/comments")
    fun getComments(@Path("readingRecordId") readingRecordId: Int): Call<CommentsResponse>

    @POST("api/reading-records/{readingRecordId}/comments")
    fun postComment(
        @Path("readingRecordId") readingRecordId: Int,
        @Body commentRequest: CommentRequest
    ): Call<CommentsResponse>

    //참여 현황 목록 조회
    @GET("/api/book-clubs/members")
    fun getParticipation(): Call<BookClubParticipationResponse>

    //서평 인증 하기
    @POST("/api/reading-records/book-clubs/{bookClubId}")
    fun postCertification(
        @Path("bookClubId") bookClubId: Int,
        @Body bookClubCertificationRequest: BookClubCertificationRequest
    ): Call<BookClubCertificationResponse>


}