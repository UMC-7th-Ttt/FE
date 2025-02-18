package com.example.fe.signup.service

import android.util.Log
import com.example.fe.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView) {
        this.signUpView = signUpView
    }

    fun signUp(user: User) {
        Log.d("AuthService", "회원가입 요청 시작")  // 로그 추가
        val authService = getRetrofit().create(AuthRetrofitInterFace::class.java)

        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("AuthService", "onResponse 호출됨")
                if (response.isSuccessful && response.body() != null) {
                    val resp: AuthResponse = response.body()!!
                    Log.d("AuthService", "회원가입 성공: $resp")

                    val id = resp.result?.id ?: -1 // result가 null일 경우 기본값 -1 설정
                    if (id != -1) {
                        signUpView.onSignUpSuccess(id) // id 값 전달
                    } else {
                        Log.e("AuthService", "회원가입 성공했지만 ID 없음")
                        signUpView.onSignUpFailure()
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                    Log.e("AuthService", "회원가입 실패 - 응답 코드: ${response.code()}, 메시지: $errorMessage")
                    signUpView.onSignUpFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("AuthService", "회원가입 요청 실패", t)
                signUpView.onSignUpFailure()
            }
        })
    }
}
