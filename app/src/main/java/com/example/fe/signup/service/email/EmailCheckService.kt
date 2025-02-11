package com.example.fe.signup.service.email

import android.util.Log
import com.example.fe.network.getRetrofit
import com.example.fe.signup.SignUpID
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmailCheckService {
    private lateinit var emailCheckView: EmailCheckView  // EmailCheckView를 사용하도록 수정

    fun setEmailCheckView(emailCheckView: EmailCheckView) {  // 매개변수도 EmailCheckView로 수정
        this.emailCheckView = emailCheckView
    }

    fun checkEmail(email: String) {
        val emailCheckService = getRetrofit().create(EmailInterface::class.java)
        emailCheckService.email(email).enqueue(object : Callback<EmailCheckResponse> {
            override fun onResponse(call: Call<EmailCheckResponse>, response: Response<EmailCheckResponse>) {
                Log.d("EMAIL_CHECK/SUCCESS", response.toString())  // 응답 정보 로그 출력

                if (!response.isSuccessful) {
                    Log.e("EMAIL_CHECK/ERROR", "서버 응답 실패: ${response.code()} - ${response.message()}")
                    emailCheckView.onEmailCheckFailure()  // 실패 시 호출
                    return
                }

                val resp: EmailCheckResponse? = response.body()
                if (resp == null) {
                    Log.e("EMAIL_CHECK/ERROR", "응답 바디가 null입니다.")
                    emailCheckView.onEmailCheckFailure()  // 응답이 null일 때 호출
                    return
                }

                Log.d("EMAIL_CHECK/RESPONSE", "응답 코드: ${resp.code}, 메시지: ${resp.message}")

                when (resp.code) {
                    "COMMON200" -> {  // 성공 코드 확인
                        Log.d("EMAIL_CHECK/SUCCESS", "이메일 사용 가능: ${resp.message}")
                        emailCheckView.onEmailCheckSuccess()  // 성공 시 호출
                    }
                    else -> {
                        Log.e("EMAIL_CHECK/FAIL", "이메일 중복 또는 오류: ${resp.message}")
                        emailCheckView.onEmailCheckFailure()  // 실패 시 호출
                    }
                }

            }

            override fun onFailure(call: Call<EmailCheckResponse>, t: Throwable) {
                Log.e("EMAIL_CHECK/FAILURE", "서버 요청 실패: ${t.message}", t)  // 실패 로그 출력
                emailCheckView.onEmailCheckFailure()  // 실패 시 호출
            }
        })
    }
}
