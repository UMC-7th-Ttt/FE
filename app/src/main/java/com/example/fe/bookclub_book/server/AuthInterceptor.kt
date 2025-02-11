package com.example.fe.bookclub_book.server

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $token") // 토큰을 헤더에 추가
            .build()
        return chain.proceed(newRequest)
    }
}
