package com.example.fe.setting

data class VerifyPasswordResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)