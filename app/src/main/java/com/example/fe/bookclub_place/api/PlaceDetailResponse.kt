package com.example.fe.bookclub_place.api

import com.google.gson.annotations.SerializedName

data class PlaceDetailResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PlaceDetail
)

data class PlaceDetail(
    @SerializedName("placeId") val placeId: Int,
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("address") val address: String,
    @SerializedName("holiday") val holiday: String?,
    @SerializedName("weekdaysBusiness") val weekdaysBusiness: String?,
    @SerializedName("sunBusiness") val sunBusiness: String?,
    @SerializedName("phone") val phone: String,
    @SerializedName("hasParking") val hasParking: Boolean,
    @SerializedName("hasCafe") val hasCafe: Boolean,
    @SerializedName("hasIndiePub") val hasIndiePub: Boolean,
    @SerializedName("hasBookClub") val hasBookClub: Boolean,
    @SerializedName("hasSpaceRental") val hasSpaceRental: Boolean,
    @SerializedName("image") val image: String,
    @SerializedName("totalRating") val totalRating: Double,
    @SerializedName("curationTitle") val curationTitle: String?,
    @SerializedName("curationContent") val curationContent: String?,
    @SerializedName("isScraped") val isScraped: Boolean,
    @SerializedName("isAdmin") val isAdmin: Boolean
)
