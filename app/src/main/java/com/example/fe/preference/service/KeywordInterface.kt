package com.example.fe.preference.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

// Retrofit 인터페이스에서 실제 HTTP 메서드를 정의합니다.
interface AuthRetrofitInterFace {

    // POST 메서드를 사용하여 '/users/keyword/{memberId}' API 주소에 데이터를 보내는 함수
    @POST("/api/users/keyword/{memberId}")  // HTTP 메서드와 API 주소, {memberId}는 path parameter
    fun signUp(
        @Path("memberId") memberId: Int,  // memberId를 URL path로 전달
        @Body user: KeywordRequest  // POST 요청에 보내는 데이터를 @Body로 설정
    ): Call<KeywordResponse>  // 응답은 KeywordResponse 형태로 받음
}
