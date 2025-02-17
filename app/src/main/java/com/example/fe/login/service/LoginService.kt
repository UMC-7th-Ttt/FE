package com.example.fe.login.service

import android.util.Log
import com.example.fe.network.getRetrofit
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginService {
    private lateinit var loginView: LoginView

    fun setLoginView(loginView: LoginView) {
        this.loginView = loginView
    }

    fun login(user: LoginRequest) {
        Log.d("LOGIN_TEST", "로그인 요청 시작: 이메일=${user.email}, 비밀번호=${user.password}")

        val authService = getRetrofit().create(LoginRetrofitInterface::class.java)
        Log.d("LOGIN_TEST", "Retrofit 객체 생성 완료")

        authService.login(user).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                Log.d("LOGIN_TEST", "HTTP 응답 코드: ${response.code()}")

                val errorBodyString = response.errorBody()?.string()
                if (errorBodyString != null) {
                    Log.e("LOGIN_TEST", "서버 응답 에러 바디: $errorBodyString")

                    try {
                        val errorJson = JSONObject(errorBodyString)
                        val message = errorJson.optString("message", "로그인 실패")
                        val errorCode = errorJson.optString("code", "UNKNOWN_ERROR")

                        Log.e("LOGIN_TEST", "에러 코드: $errorCode, 메시지: $message")
                        loginView.loginFailure(message)
                    } catch (e: Exception) {
                        Log.e("LOGIN_TEST", "에러 바디 JSON 파싱 오류: ${e.message}")
                        loginView.loginFailure("서버 오류 발생")
                    }
                    return
                }

                val resp: LoginResponse? = response.body()
                if (resp != null) {
                    Log.d("LOGIN_TEST", "서버 응답 코드: ${resp.code}")
                    when (resp.code) {
                        "COMMON200" -> {
                            loginView.loginSuccess()

                            //accessToken 저장 추가
                            val accessToken = resp.result?.accessToken
                            if (!accessToken.isNullOrEmpty()) {
                                Log.d("LOGIN_TEST", "accessToken 저장 완료: $accessToken")
                                loginView.saveAuthToken(accessToken)
                            } else {
                                Log.e("LOGIN_TEST", "accessToken이 응답에 없음")
                            }

                            //Authorization 헤더의 토큰도 저장 (기존 로직 유지)
                            val authToken = response.headers()["Authorization"]
                            if (!authToken.isNullOrEmpty()) {
                                Log.d("LOGIN_TEST", "Authorization 토큰 저장 완료: $authToken")
                                loginView.saveAuthToken(authToken)
                            } else {
                                Log.e("LOGIN_TEST", "Authorization 토큰이 응답에 없음")
                            }
                        }
                        else -> loginView.loginFailure(resp.message ?: "로그인 실패")
                    }
                } else {
                    Log.e("LOGIN_TEST", "응답 바디가 null입니다.")
                    loginView.loginFailure("서버 응답이 없습니다. (코드: ${response.code()})")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LOGIN_TEST", "네트워크 오류: ${t.message}", t)
                loginView.loginFailure("네트워크 오류 발생")
            }
        })
    }
}
