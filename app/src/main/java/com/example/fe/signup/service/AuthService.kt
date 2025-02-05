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
                Log.d("AuthService", "onResponse 호출됨")  // 로그 추가
                if (response.isSuccessful) {
                    Log.d("AuthService", "회원가입 성공: ${response.body()}")
                    signUpView?.onSignUpSuccess()
                } else {
                    // ✅ 서버 응답 메시지 확인
                    val errorMessage = response.errorBody()?.string()
                    Log.e("AuthService", "회원가입 실패 - 응답 코드: ${response.code()}, 메시지: $errorMessage")
                    signUpView?.onSignUpFailure()
                }
                Log.d("SIGNUP/SUCCESS", response.toString())
                val resp: AuthResponse = response.body()!!

                when(resp.code) {
                    1000.toString() -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.e("AuthService", "회원가입 요청 실패", t)
                signUpView.onSignUpFailure()
            }
        })
    }
}
