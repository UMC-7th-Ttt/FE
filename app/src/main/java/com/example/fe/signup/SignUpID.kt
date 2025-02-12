package com.example.fe.signup

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.fe.R
import com.example.fe.databinding.ActivitySignUpIdBinding
import com.example.fe.signup.service.email.*

class SignUpID : AppCompatActivity(), EmailCheckView, AuthCodeRequestView, AuthCodeVerifyView {
    private lateinit var binding: ActivitySignUpIdBinding
    private val emailCheckService = EmailCheckService()
    private val authCodeSendService = AuthCodeSendService()
    private val authCodeVerifyService = AuthCodeVerifyService()

    private var isEmailValid = false
    private var isEmailExist = false
    private var verificationCodeSent = false
    private var isCodeVerified = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpIdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        emailCheckService.setEmailCheckView(this)
        authCodeSendService.setAuthCodeSendView(this)
        authCodeVerifyService.setAuthCodeVerifyView(this)

        setupUI()
        setupListeners()
    }

    private fun setupUI() {
        // 처음에는 오류 메시지 숨김
        binding.emailError.visibility = View.GONE
        binding.accountExistsError.visibility = View.GONE
        binding.verificationCodeError.visibility = View.GONE
        binding.verificationCodeInput.visibility = View.GONE
        binding.emailLabel2.visibility = View.GONE
        binding.timerText.visibility = View.GONE
    }

    private fun setupListeners() {

        // 이메일 입력 감지
        binding.pwCheckInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val email = s.toString()
                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

                if (!isEmailValid) {
                    binding.emailError.visibility = View.VISIBLE
                    binding.emailError.text = "이메일 형식이 올바르지 않습니다."
                    binding.pwCheckInput.setBackgroundResource(R.drawable.background_login_input_error)
                } else {
                    binding.emailError.visibility = View.GONE
                    binding.pwCheckInput.setBackgroundResource(R.drawable.background_login_input_normal)
                    emailCheckService.checkEmail(email) // 이메일 중복 검사 API 호출
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // 인증번호 요청 버튼
        binding.sendButton.setOnClickListener {
            val email = binding.pwCheckInput.text.toString()
            if (isEmailValid && !isEmailExist) {
                authCodeSendService.sendAuthCode(email) // 인증번호 요청 API 호출
            }
        }

        // 인증번호 입력 감지
        binding.verificationCodeInput.addTextChangedListener {
            if (verificationCodeSent && it.toString().length == 6) {
                val email = binding.pwCheckInput.text.toString()
                val code = it.toString()
                authCodeVerifyService.verifyAuthCode(email, code) // 인증번호 검증 API 호출
            }
        }

        // 다음 버튼
        binding.nextButton.setOnClickListener {
            if (isCodeVerified) {
                val email = binding.pwCheckInput.text.toString()
                val intent = Intent(this, SignUpPW::class.java)
                intent.putExtra("email", email)
                startActivity(intent)
            } else {
                Toast.makeText(this, "이메일 인증을 완료해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 타이머 시작 함수
    private fun startTimer() {
        object : CountDownTimer(3 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                binding.timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                binding.timerText.text = "00:00"
                verificationCodeSent = false
            }
        }.start()
    }

    // 이메일 중복 검사 결과 처리
    override fun onEmailCheckSuccess() {
        isEmailExist = false
        binding.accountExistsError.visibility = View.GONE
        binding.sendButton.isEnabled = true
    }

    override fun onEmailCheckFailure() {
        isEmailExist = true
        binding.accountExistsError.visibility = View.VISIBLE
        binding.accountExistsError.text = "이미 가입된 계정이 있습니다."
        binding.sendButton.isEnabled = false
    }

    // 인증번호 요청 결과 처리
    override fun onAuthCodeRequestSuccess() {
        verificationCodeSent = true
        binding.verificationCodeInput.visibility = View.VISIBLE
        binding.emailLabel2.visibility = View.VISIBLE
        binding.timerText.visibility = View.VISIBLE
        startTimer()
    }

    override fun onAuthCodeRequestFailure() {
        Toast.makeText(this, "인증번호 전송 실패. 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
    }

    // 인증번호 검증 결과 처리
    override fun onAuthCodeVerifySuccess() {
        isCodeVerified = true
        binding.verificationCodeError.visibility = View.GONE
        Toast.makeText(this, "인증번호 확인 완료!", Toast.LENGTH_SHORT).show()
    }

    override fun onAuthCodeVerifyFailure() {
        isCodeVerified = false
        binding.verificationCodeError.visibility = View.VISIBLE
        binding.verificationCodeError.text = "인증번호가 틀렸습니다. 다시 입력해주세요."
    }
}
