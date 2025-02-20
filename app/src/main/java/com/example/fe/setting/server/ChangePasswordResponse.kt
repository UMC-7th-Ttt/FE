package com.example.fe.setting.server

data class ChangePasswordResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String?
)