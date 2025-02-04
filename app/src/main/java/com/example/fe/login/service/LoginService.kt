package com.example.fe.login.service

import android.util.Log
import com.example.fe.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService {
    private lateinit var loginView: LoginView

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun login(user: LoginRequest) {
        Log.d("LOGIN/REQUEST", "로그인 요청 시작: 이메일=${user.email}, 비밀번호=${user.password}")

        val authService = getRetrofit().create(LoginRetrofitInterface::class.java)
        Log.d("LOGIN/RETROFIT", "Retrofit 객체 생성 완료")

        authService.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LOGIN/RESPONSE", "HTTP 응답 코드: ${response.code()}")

                val rawResponse = response.errorBody()?.string() ?: response.body().toString()
                Log.d("LOGIN/RAW_RESPONSE", "원본 응답: $rawResponse")

                try {
                    val resp: LoginResponse? = response.body()
                    if (resp != null) {
                        Log.d("LOGIN/RESPONSE", "서버 응답 코드: ${resp.code}")
                        when (resp.code) {
                            "COMMON200" -> loginView.loginSuccess()
                            else -> loginView.loginFailure(resp.message ?: "로그인 실패")
                        }
                    } else {
                        Log.e("LOGIN/ERROR", "응답 바디가 null입니다.")
                        loginView.loginFailure("서버 응답이 없습니다.")
                    }
                } catch (e: Exception) {
                    Log.e("LOGIN/PARSE_ERROR", "JSON 파싱 오류: ${e.message}", e)
                    loginView.loginFailure("데이터 처리 오류 발생")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LOGIN/FAILURE", "네트워크 오류: ${t.message}", t)
                loginView.loginFailure("네트워크 오류 발생")
            }
        })
    }
}
