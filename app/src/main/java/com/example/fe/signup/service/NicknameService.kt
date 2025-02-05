package com.example.fe.signup.service

import android.util.Log
import com.example.fe.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

class NicknameService {
    private lateinit var nicknameView: NicknameView

    fun setNicknameView(nicknameView: NicknameView) {
        this.nicknameView = nicknameView
    }

    fun nickName(nickname: String) {
        val nicknameService = getRetrofit().create(NicknameRetrofitInterface::class.java)
        nicknameService.checkNicknameDuplicated(nickname).enqueue(object : Callback<NicknameResponse> {
            override fun onResponse(call: Call<NicknameResponse>, response: Response<NicknameResponse>) {
                Log.d("NICKNAME/SUCCESS", response.toString())  // 응답 정보 로그 출력

                if (!response.isSuccessful) {
                    Log.e("NICKNAME/ERROR", "서버 응답 실패: ${response.code()} - ${response.message()}")
                    nicknameView.nicknameCheckFailure()
                    return
                }

                val resp: NicknameResponse? = response.body()
                if (resp == null) {
                    Log.e("NICKNAME/ERROR", "응답 바디가 null입니다.")
                    nicknameView.nicknameCheckFailure()
                    return
                }

                Log.d("NICKNAME/RESPONSE", "응답 코드: ${resp.code}, 메시지: ${resp.message}")

                when (resp.code) {
                    "COMMON200" -> {  // 성공 코드 확인
                        Log.d("NICKNAME/SUCCESS", "닉네임 사용 가능: ${resp.message}")
                        nicknameView.nicknameCheckSuccess()
                    }
                    else -> {
                        Log.e("NICKNAME/FAIL", "닉네임 중복 또는 오류: ${resp.message}")
                        nicknameView.nicknameCheckFailure()
                    }
                }

            }

            override fun onFailure(call: Call<NicknameResponse>, t: Throwable) {
                Log.e("NICKNAME/FAILURE", "서버 요청 실패: ${t.message}", t)  // 실패 로그 출력
                nicknameView.nicknameCheckFailure()
            }
        })
    }
}
