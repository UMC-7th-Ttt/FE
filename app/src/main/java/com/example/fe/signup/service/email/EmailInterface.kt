package com.example.fe.signup.service.email

import com.example.fe.signup.service.NicknameResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface EmailInterface {


    @POST("/api/email-duplicated/{email}")
    fun email( @Path("email") email: String): Call<EmailCheckResponse>

    @POST("/api/users/code")
    fun coderequest(@Body request: AuthCodeRequestRequest): Call<AuthCodeRequestResponse>

    @POST("/api/users/verify-code")
    fun codeverify(@Body request: AuthCodeVerifyRequest): Call<AuthCodeVerifyResponse>


}

