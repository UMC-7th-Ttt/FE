package com.example.fe.BookDetail.Review

data class UserReview(
    val profileImage: Int,  // 프로필 이미지 리소스 ID
    val userName: String,   // 유저 이름
    val reviewText: String  // 서평 내용
)
