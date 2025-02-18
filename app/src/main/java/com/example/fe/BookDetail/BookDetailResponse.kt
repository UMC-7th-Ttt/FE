package com.example.fe.BookDetail

import com.google.gson.annotations.SerializedName

data class BookDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: BookDetail
)

data class BookDetail(
    @SerializedName("id") val id: Long,
    @SerializedName("cover") val cover: String,
    @SerializedName("title") val title: String,
    @SerializedName("author") val author: String,
    @SerializedName("category") val category: String,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("itemPage") val itemPage: Int,
    @SerializedName("description") val description: String,
    @SerializedName("hasEbook") val hasEbook: Boolean,
    @SerializedName("userRating") val userRating: Double,
    @SerializedName("totalRating") val totalRating: Double,
    @SerializedName("reviews") val reviews: List<BookReview>
)

data class BookReview(
    @SerializedName("id") val id: Long,
    @SerializedName("content") val content: String,
    @SerializedName("rating") val rating: Long,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("memberInfo") val memberInfo: MemberInfo
)

data class MemberInfo(
    @SerializedName("id") val id: Long,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileUrl") val profileUrl: String
)
data class UserReview(
    val profileImage: Int,  // 프로필 이미지 리소스 ID
    val userName: String,   // 유저 이름
    val reviewText: String  // 서평 내용
)