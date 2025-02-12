package com.example.fe.mypage

import com.example.fe.bookclub_book.dataclass.BookClubBookJoin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyPageRetrofitInterface {
    @GET("/api/scraps/folders")
    fun getFolders(): Call<ScrapFolderResponse>

}