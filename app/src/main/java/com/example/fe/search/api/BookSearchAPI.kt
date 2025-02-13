package com.example.fe.search.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookSearchAPI {
    // 도서 검색
    @GET("api/books/search")
    fun searchBooks(
        @Query("keyword") keyword: String,
        @Query("cursor") cursor: Int = 0,
        @Query("limit") limit: Int = 10
    ): Call<BookSearchResponse>

    // 카테고리별 인기도서
    @GET("api/books/search/suggestions")
    fun getBookSuggestions(
        @Query("categoryName") categoryName: String
    ): Call<BookSuggestionResponse>

    // 사용자 추천 도서
    @GET("api/books/search/user-suggestions")
    fun getUserBookSuggestions(): Call<BookUserSuggestionResponse>

    // 에디터 픽 도서
    @GET("api/books/search/editor-pick")
    fun getEditorPickBooks(): Call<BookEditorPickResponse>
}
