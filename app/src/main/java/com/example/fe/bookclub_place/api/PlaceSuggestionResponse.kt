package com.example.fe.bookclub_place.api

import com.google.gson.annotations.SerializedName

data class PlaceSuggestionResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PlaceSuggestion
)

data class PlaceSuggestion(
    @SerializedName("places") val places: List<PlaceResponse>
)
