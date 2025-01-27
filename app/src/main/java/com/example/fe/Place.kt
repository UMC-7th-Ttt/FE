package com.example.fe

data class Place(
    val name: String,
    val tag: String,         // 장소 태그 (예: 카페, 서점 등)
    val rating: Double,      // 평점
    val imageResId: Int,
    var isBookmarked: Boolean
)
