package com.example.fe.signup.service

interface NicknameView {
    fun nicknameCheckSuccess()  //  성공 시 호출
    fun nicknameCheckFailure()  //  실패 시 호출
}