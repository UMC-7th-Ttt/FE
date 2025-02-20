package com.example.fe.Review

import com.google.gson.annotations.SerializedName


data class ReviewDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result
) {
    data class Result(
        @SerializedName("id") val id: Int,
        @SerializedName("title") val title: String,
        @SerializedName("content") val content: String,
        @SerializedName("isSecret") val isSecret: Boolean,
        @SerializedName("bookRanking") val bookRanking: Int,
        @SerializedName("placeRanking") val placeRanking: Int,
        @SerializedName("writeDate") val writeDate: String,
        @SerializedName("book") val book: Book,
        @SerializedName("place") val place: Place,
        @SerializedName("isWriter") val isWriter: Boolean
    ) {
        data class Book(
            @SerializedName("id") val id: Int,
            @SerializedName("title") val title: String,
            @SerializedName("author") val author: String,
            @SerializedName("cover") val cover: String
        )

        data class Place(
            @SerializedName("id") val id: Int,
            @SerializedName("title") val title: String,
            @SerializedName("address") val address: String,
            @SerializedName("image") val image: String
        )
    }
}