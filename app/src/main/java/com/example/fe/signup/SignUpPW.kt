package com.example.fe.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fe.R
import com.example.fe.databinding.ActivitySignUpPwBinding
import com.example.fe.signup.service.AuthService
import com.example.fe.signup.service.SignUpView
import com.example.fe.signup.service.User

// 1단계: SignUpView 인터페이스를 구현하는 MainActivity 클래스
class SignUpPW : AppCompatActivity(), SignUpView {

    private lateinit var pwInput: EditText
    private lateinit var pwCheckInput: EditText
    private lateinit var binding: ActivitySignUpPwBinding // ViewBinding 객체 선언

    // onCreate()는 액티비티가 생성될 때 호출되는 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPwBinding.inflate(layoutInflater) // ViewBinding으로 레이아웃 바인딩
        setContentView(binding.root) // 레이아웃을 액티비티에 설정

        pwInput = binding.pwInput
        pwCheckInput = binding.pwCheckInput

        // 비밀번호 숨기기 설정 (기본적으로 숨겨짐)
        pwInput.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
        pwCheckInput.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()


        // 비밀번호 보이기/숨기기
        binding.pwShow.setOnClickListener {
            if (pwInput.transformationMethod == android.text.method.PasswordTransformationMethod.getInstance()) {
                pwInput.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance() // 비밀번호 보이기
            } else {
                pwInput.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance() // 비밀번호 숨기기
            }
            // 커서를 텍스트 끝으로 이동
            pwInput.setSelection(pwInput.text.length)
        }

        // 비밀번호 보이기/숨기기
        binding.pwCheckShow.setOnClickListener {
            if (pwCheckInput.transformationMethod == android.text.method.PasswordTransformationMethod.getInstance()) {
                pwCheckInput.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance() // 비밀번호 보이기
            } else {
                pwCheckInput.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance() // 비밀번호 숨기기
            }
            // 커서를 텍스트 끝으로 이동
            pwCheckInput.setSelection(pwCheckInput.text.length)
        }

        // 회원가입 버튼 클릭 시 실행될 코드
        binding.nextButton.setOnClickListener {
            signUp() // 회원가입 함수 호출
        }

        // 비밀번호 길이 체크
        binding.pwInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = s.toString()
                if (password.length in 6..12) {
                    binding.pwCheck1Tv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
                    binding.pwCheck1.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
                    binding.pwCheck2Tv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
                    binding.pwCheck2.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
                } else {
                    binding.pwCheck1Tv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.white_20))
                    binding.pwCheck1.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.white_20))
                }

//                // 대소문자, 숫자, 특수문자 조건을 만족하는지 확인
//                val passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{6,12}$")
//                val matcher = passwordPattern.matcher(password)
//                if (matcher.matches()) {
//                    binding.pwCheck2Tv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
//                    binding.pwCheck2.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
//
//                } else {
//                    binding.pwCheck2Tv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.white_20))
//                    binding.pwCheck2.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.white_20))
//                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 비밀번호 확인 입력 시, 비밀번호와 일치하는지 확인
        binding.pwCheckInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val password = binding.pwInput.text.toString()
                val confirmPassword = s.toString()

                if (password == confirmPassword) {
                    binding.pwCheckCheck.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
                    binding.pwCheckCheckTv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.primary_50))
                    binding.nextButton.isEnabled = true // Next 버튼 활성화
                } else {
                    binding.pwCheckCheck.setColorFilter(ContextCompat.getColor(this@SignUpPW, R.color.white_20))
                    binding.pwCheckCheckTv.setTextColor(ContextCompat.getColor(this@SignUpPW, R.color.white_20))
                    binding.nextButton.isEnabled = false // Next 버튼 비활성화
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun getUser(): User {
        val email = intent.getStringExtra("email") ?: ""  // 이메일 값 받기
        val password = binding.pwInput.text.toString()  // 비밀번호 값 가져오기
        return User(email, password)
    }

    // 회원가입을 처리하는 함수
    private fun signUp() {
        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        if (binding.pwInput.text.toString() != binding.pwCheckInput.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        // User 객체  생성
        val user = getUser()

        // 전송할 값 확인 (로그 출력)
        Log.d("SignUp", "Email: ${user.email}, Password: ${user.password}")

        // 2단계: AuthService 객체 생성 후 회원가입 요청
        val authService = AuthService() // AuthService 객체 생성
        authService.setSignUpView(this) // 현재 액티비티를 SignUpView로 설정
        authService.signUp(getUser()) // 회원가입 요청
    }

    // 1단계: SignUpView 인터페이스에서 정의한 onSignUpSuccess 메서드 구현
    override fun onSignUpSuccess(id: Int) {
        Toast.makeText(this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show() // 회원가입 성공 메시지 표시

        // 다른 페이지로 이동
        val intent = Intent(this, SignUpName::class.java)
        intent.putExtra("id", id)
        startActivity(intent)

    }

    override fun onSignUpFailure() {
        Log.e("SignUpError", "회원가입 요청이 실패했습니다.")

        Toast.makeText(this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show()

        val errorMessage = "서버 응답을 확인하세요"  // 필요시 서버 응답을 받아서 메시지를 표시할 수도 있음
        Toast.makeText(this, "오류: $errorMessage", Toast.LENGTH_LONG).show()
    }
}
