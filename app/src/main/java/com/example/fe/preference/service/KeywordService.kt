package com.example.fe.preference.service

import android.util.Log
import com.example.fe.network.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// KeywordService는 실제 Retrofit 요청을 처리하는 클래스입니다.
class KeywordService {

    // 1단계: View 연결을 위한 변수
    private lateinit var keywordView: KeywordView

    // 2단계: KeywordView를 연결하는 함수
    fun setKeywordView(keywordView: KeywordView) {
        this.keywordView = keywordView
    }

    // 3단계: Retrofit을 사용한 키워드 처리 함수
    fun handleKeywordRequest(memberId: String, user: KeywordRequest) {
        val keywordService = getRetrofit().create(AuthRetrofitInterFace::class.java)  // Retrofit 객체를 생성하고, 인터페이스 연결

//        keywordService.keyword(memberId, user).enqueue(object : Callback<KeywordResponse> {
//            // 서버 응답 성공 시 호출
//            override fun onResponse(call: Call<KeywordResponse>, response: Response<KeywordResponse>) {
//                Log.d("KEYWORD/SUCCESS", response.toString())  // 응답 정보 로그 출력
//                val resp: KeywordResponse = response.body()!!  // 응답 바디를 KeywordResponse 객체로 변환
//
//                // 응답 코드에 따른 처리
//                when (resp.code) {
//                    "COMMON200" -> keywordView.onKeywordSuccess()  // 성공 시 View에 성공 알림
//                    else -> keywordView.onKeywordFailure("fail")  // 실패 시 View에 실패 알림
//                }
//            }
//
//            // 서버 요청 실패 시 호출
//            override fun onFailure(call: Call<KeywordResponse>, t: Throwable) {
//                Log.d("KEYWORD/FAILURE", t.message.toString())  // 실패 로그 출력
//                keywordView.onKeywordFailure("fail")  // 실패 시 View에 실패 알림
//            }
//        })
    }
}
