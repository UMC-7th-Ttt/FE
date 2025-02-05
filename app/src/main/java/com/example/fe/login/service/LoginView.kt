package com.example.fe.login.service

interface LoginView {
    fun loginSuccess()  // 회원가입 성공 시 호출
    fun loginFailure(message: String)  // 회원가입 실패 시 호출
}