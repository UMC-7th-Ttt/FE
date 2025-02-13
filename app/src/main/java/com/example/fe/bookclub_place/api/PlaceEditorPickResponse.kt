package com.example.fe.bookclub_place.api

import com.google.gson.annotations.SerializedName

data class PlaceEditorPickResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PlaceEditorPickResult
)

data class PlaceEditorPickResult(
    @SerializedName("places") val places: List<PlaceResponse>
)
