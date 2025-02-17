package com.example.fe.BookLetter

data class Book(
    val title: String,         // 책 제목
    val author: String,        // 저자
    val publisher: String,     // 출판사
    val imageResId: String        // 책 표지 이미지 (리소스 ID)
)
