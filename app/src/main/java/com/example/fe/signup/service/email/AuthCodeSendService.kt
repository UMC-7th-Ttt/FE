package com.example.fe.signup.service.email

import android.util.Log
import com.example.fe.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthCodeSendService {
    private lateinit var authCodeSendView: AuthCodeRequestView  // AuthCodeSendView 인터페이스 정의

    fun setAuthCodeSendView(authCodeSendView: AuthCodeRequestView) {
        this.authCodeSendView = authCodeSendView  // 인터페이스 객체 초기화
    }

    fun sendAuthCode(email: String) {
        val authCodeService = getRetrofit().create(EmailInterface::class.java)
        val request = AuthCodeRequestRequest(email)  // 이메일을 이용한 인증번호 요청 객체 생성
        authCodeService.coderequest(request).enqueue(object : Callback<AuthCodeRequestResponse> {
            override fun onResponse(call: Call<AuthCodeRequestResponse>, response: Response<AuthCodeRequestResponse>) {
                Log.d("AUTH_CODE_SEND/SUCCESS", response.toString())  // 응답 정보 로그 출력

                if (!response.isSuccessful) {
                    Log.e("AUTH_CODE_SEND/ERROR", "서버 응답 실패: ${response.code()} - ${response.message()}")
                    authCodeSendView.onAuthCodeRequestFailure()  // 실패 시 호출
                    return
                }

                val resp: AuthCodeRequestResponse? = response.body()
                if (resp == null) {
                    Log.e("AUTH_CODE_SEND/ERROR", "응답 바디가 null입니다.")
                    authCodeSendView.onAuthCodeRequestFailure()  // 응답이 null일 경우
                    return
                }

                Log.d("AUTH_CODE_SEND/RESPONSE", "응답 코드: ${resp.code}, 메시지: ${resp.message}")

                when (resp.code) {
                    "COMMON200" -> {  // 성공 코드 확인
                        Log.d("AUTH_CODE_SEND/SUCCESS", "인증번호 발송 성공: ${resp.result}")
                        authCodeSendView.onAuthCodeRequestSuccess()  // 성공 시 호출
                    }
                    else -> {
                        Log.e("AUTH_CODE_SEND/FAIL", "인증번호 발송 실패: ${resp.message}")
                        authCodeSendView.onAuthCodeRequestFailure()  // 실패 시 호출
                    }
                }

            }

            override fun onFailure(call: Call<AuthCodeRequestResponse>, t: Throwable) {
                Log.e("AUTH_CODE_SEND/FAILURE", "서버 요청 실패: ${t.message}", t)  // 실패 로그 출력
                authCodeSendView.onAuthCodeRequestFailure()  // 실패 시 호출
            }
        })
    }
}
