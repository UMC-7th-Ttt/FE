package com.example.fe

data class Book(
    val imageResId: Int,
    val title: String,
    val author: String,
    val letterTitle: String,
    var isBookmarked: Boolean
)