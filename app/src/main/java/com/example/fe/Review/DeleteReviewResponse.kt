package com.example.fe.Review

import com.google.gson.annotations.SerializedName

data class DeleteReviewResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: String,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Any?
)