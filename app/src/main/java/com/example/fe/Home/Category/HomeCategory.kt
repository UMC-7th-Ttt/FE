package com.example.fe.Home.Category

data class HomeCategory(
    val title: String,     // 카테고리 이름
    val books: List<HomeBook>  // 해당 카테고리 내의 책 리스트
)
