package com.example.fe.signup.service.email

import android.util.Log
import com.example.fe.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthCodeVerifyService {
    private lateinit var authCodeVerifyView: AuthCodeVerifyView

    fun setAuthCodeVerifyView(authCodeVerifyView: AuthCodeVerifyView) {
        this.authCodeVerifyView = authCodeVerifyView
    }

    fun verifyAuthCode(email: String, authCode: String) {
        val authCodeVerifyService = getRetrofit().create(EmailInterface::class.java)
        authCodeVerifyService.codeverify(AuthCodeVerifyRequest(email, authCode)).enqueue(object : Callback<AuthCodeVerifyResponse> {
            override fun onResponse(call: Call<AuthCodeVerifyResponse>, response: Response<AuthCodeVerifyResponse>) {
                Log.d("AUTH_CODE_VERIFY/SUCCESS", response.toString())  // 응답 정보 로그 출력

                if (!response.isSuccessful) {
                    Log.e("AUTH_CODE_VERIFY/ERROR", "서버 응답 실패: ${response.code()} - ${response.message()}")
                    authCodeVerifyView.onAuthCodeVerifyFailure()
                    return
                }

                val resp: AuthCodeVerifyResponse? = response.body()
                if (resp == null) {
                    Log.e("AUTH_CODE_VERIFY/ERROR", "응답 바디가 null입니다.")
                    authCodeVerifyView.onAuthCodeVerifyFailure()
                    return
                }

                Log.d("AUTH_CODE_VERIFY/RESPONSE", "응답 코드: ${resp.code}, 메시지: ${resp.message}")

                when (resp.code) {
                    "COMMON200" -> {  // 성공 코드 확인
                        Log.d("AUTH_CODE_VERIFY/SUCCESS", "인증번호 검증 성공: ${resp.message}")
                        authCodeVerifyView.onAuthCodeVerifySuccess()
                    }
                    else -> {
                        Log.e("AUTH_CODE_VERIFY/FAIL", "인증번호 검증 실패: ${resp.message}")
                        authCodeVerifyView.onAuthCodeVerifyFailure()
                    }
                }

            }

            override fun onFailure(call: Call<AuthCodeVerifyResponse>, t: Throwable) {
                Log.e("AUTH_CODE_VERIFY/FAILURE", "서버 요청 실패: ${t.message}", t)  // 실패 로그 출력
                authCodeVerifyView.onAuthCodeVerifyFailure()
            }
        })
    }
}