package com.example.fe.setting.server

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part

interface SettingRetrofitInterface {

    //프로필 변경
    @Multipart
    @PATCH("/api/users")
    fun changeProfile(
        @Part("requestDTO") requestDTO: RequestBody,
        @Part profilePicture: MultipartBody.Part?
    ): Call<ChangeProfileResponse>

    //비밀번호 확인
    @POST("/api/users/verify-pw")
    fun verifyPassword(
        @Body password: RequestBody
    ): Call<VerifyPasswordResponse>

    //비밀번호 변경
    @PATCH("/api/users/password")
    fun changePassword(
        @Body password: RequestBody
    ): Call<ChangePasswordResponse>

    @POST("/api/logout")
    suspend fun logout(
        @Header("Authorization") accessToken: String
    ): Response<LogoutResponse>

}