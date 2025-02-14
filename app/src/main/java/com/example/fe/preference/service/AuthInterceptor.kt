package com.example.fe.preference.service

import okhttp3.Interceptor
import okhttp3.Response
import android.content.SharedPreferences
import android.util.Log

class AuthInterceptor(private val sharedPreferences: SharedPreferences) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // SharedPreferences에서 토큰을 가져오기
        val authToken = sharedPreferences.getString("accessToken", null)

        // 토큰이 있을 경우 Authorization 헤더 추가
        return if (!authToken.isNullOrEmpty()) {
            val newRequest = originalRequest.newBuilder()
                .header("Authorization", "Bearer $authToken") // Authorization 헤더 추가
                .build()
            Log.d("BookResponseError", "Authorization Header: Bearer $authToken") // 디버깅용 로그
            chain.proceed(newRequest)
        } else {
            Log.d("BookResponseError", "토큰이 없습니다. Authorization 헤더를 추가하지 않습니다.") // 토큰이 없을 때의 로그
            chain.proceed(originalRequest) // 토큰이 없으면 기존 요청 그대로 진행
        }
    }
}

