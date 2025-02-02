package com.example.fe.bookclub_book

import com.example.fe.bookclub_book.dataclass.GetBookClubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BookClubRetrofitInterface {
    @GET("/api/book-clubs/{bookClubId}/join")
    fun getBookClubs(@Path("bookClubId") bookClubId: Int): Call<GetBookClubResponse>
}
