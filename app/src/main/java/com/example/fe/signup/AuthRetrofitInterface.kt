package com.example.fe.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterFace {
    @POST("api/sign-up") // @Method(api address)
    fun signUp(@Body user:User): Call<AuthResponse>

}