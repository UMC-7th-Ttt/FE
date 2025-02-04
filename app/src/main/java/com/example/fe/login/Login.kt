package com.example.fe.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.login.service.LoginRequest
import com.example.fe.login.service.LoginService
import com.example.fe.login.service.LoginView
import com.example.fe.signup.TermsofUse

class Login : AppCompatActivity(), LoginView {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var signUpButton: TextView
    private lateinit var autoLoginCheckbox: CheckBox
    private lateinit var sharedPreferences: android.content.SharedPreferences
    private lateinit var loginService: LoginService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.id_input)
        passwordInput = findViewById(R.id.pw_input)
        loginButton = findViewById(R.id.login_button)
        autoLoginCheckbox = findViewById(R.id.login_checkbox)
        signUpButton = findViewById(R.id.signupButton)

        // SharedPreferences 초기화
        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        autoLoginCheckbox.isChecked = sharedPreferences.getBoolean("autoLogin", false)

        // LoginService 초기화 및 View 연결
        loginService = LoginService()
        loginService.setLoginView(this)

        // 로그인 버튼 클릭 이벤트
        loginButton.setOnClickListener {
            login()
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, TermsofUse::class.java)
            startActivity(intent)
        }

        // 비밀번호 숨기기 설정 (기본적으로 숨겨짐)
        passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
        val pwShow = findViewById<ImageView>(R.id.pw_show)

        // 비밀번호 보이기/숨기기 토글 기능
        pwShow.setOnClickListener {
            if (passwordInput.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordInput.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance()
            } else {
                passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            passwordInput.setSelection(passwordInput.text.length)
        }
    }

    // 로그인 API 요청 함수
    private fun login() {
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val user = LoginRequest(email, password)

        Log.d("LOGIN/REQUEST", "로그인 요청: 이메일=$email, 비밀번호=****")

        try {
            loginService.login(user)
        } catch (e: Exception) {
            Log.e("LOGIN/ERROR", "로그인 요청 중 예외 발생: ${e.message}")
            Toast.makeText(this, "로그인 요청 실패", Toast.LENGTH_SHORT).show()
        }
    }

    // 로그인 성공 시 실행
    override fun loginSuccess() {
        Log.d("LOGIN/SUCCESS", "로그인 성공")
        Toast.makeText(this, "로그인 성공!", Toast.LENGTH_SHORT).show()
        // TODO: 홈 화면 이동 로직 추가
    }

    // 로그인 실패 시 실행
    override fun loginFailure(message: String) {
        Log.e("LOGIN/FAILURE", "로그인 실패: $message")
        Toast.makeText(this, "로그인 실패: $message", Toast.LENGTH_SHORT).show()
    }
}
