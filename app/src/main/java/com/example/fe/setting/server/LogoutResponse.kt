package com.example.fe.setting.server

data class LogoutResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String
)