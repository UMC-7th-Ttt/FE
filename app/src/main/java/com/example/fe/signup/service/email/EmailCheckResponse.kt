package com.example.fe.signup.service.email

import com.google.gson.annotations.SerializedName

data class EmailCheckResponse (
    // JSON 필드명을 맞추기 위해 @SerializedName을 사용하여 JSON의 key와 변수명을 연결
    @SerializedName(value = "isSuccess") val isSuccess: Boolean,  // 성공 여부
    @SerializedName(value = "code") val code: String,  // 응답 코드
    @SerializedName(value = "message") val message: String,  // 응답 메시지
    @SerializedName(value = "result") val result: Any?
)