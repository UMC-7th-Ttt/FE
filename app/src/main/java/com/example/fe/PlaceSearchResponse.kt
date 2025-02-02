package com.example.fe

import com.google.gson.annotations.SerializedName

data class PlaceSearchResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Result
)

data class Result(
    @SerializedName("places") val places: List<PlaceResponse>,
    @SerializedName("cursor") val cursor: Int,
    @SerializedName("limit") val limit: Int,
    @SerializedName("hasNext") val hasNext: Boolean
)
