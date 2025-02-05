package com.example.fe.signup.service.email

// 인증번호 검증 결과를 처리하기 위한 인터페이스입니다.
interface EmailCheckView {
    fun onEmailCheckSuccess()  // 인증번호 검증 성공 시 호출
    fun onEmailCheckFailure()  // 인증번호 검증 실패 시 호출
}

// 인증번호 검증 결과를 처리하기 위한 인터페이스입니다.
interface AuthCodeRequestView {
    fun onAuthCodeRequestSuccess()  // 인증번호 검증 성공 시 호출
    fun onAuthCodeRequestFailure()  // 인증번호 검증 실패 시 호출
}

// 인증번호 검증 결과를 처리하기 위한 인터페이스입니다.
interface AuthCodeVerifyView {
    fun onAuthCodeVerifySuccess()  // 인증번호 검증 성공 시 호출
    fun onAuthCodeVerifyFailure()  // 인증번호 검증 실패 시 호출
}