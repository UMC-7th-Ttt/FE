package com.example.fe.bookclub_place.api

data class PlaceSortingRequest (
    val lat: Double, // 위도 - 현재 위치의 위도. 가까운순으로 조회할 때만 전달
    val lon: Double, // 경도 - 현재 위치의 위도. 가까운순으로 조회할 때만 전달
    val sort: String = "all", // 전체: all, 서점: bookstore, 북카페: cafe
    val cursor: Int = 0,
    val limit: Int = 10
)