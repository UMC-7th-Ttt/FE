package com.example.fe.signup.service

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Path

interface NicknameRetrofitInterface {

    // 닉네임 중복 체크 API
    @POST("/api/nickname-duplicated/{nickname}")
    fun checkNicknameDuplicated(
        @Path("nickname") nickname: String
    ): Call<NicknameResponse>
}