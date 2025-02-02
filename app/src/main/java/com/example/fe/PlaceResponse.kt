package com.example.fe

data class PlaceResponse(
    val placeId: Int,        // 장소 ID
    val title: String,       // 장소 이름
    val category: String,    // 카테고리 (카페, 서점 등)
    val address: String,     // 주소
    val image: String,       // 이미지 URL
    val totalRating: Double, // 평점
    val isScraped: Boolean   // 북마크 여부
)
