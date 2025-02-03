package com.example.fe.signup

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.R
import com.example.fe.databinding.ActivitySignUpPwBinding
import java.util.regex.Pattern

// 1단계: SignUpView 인터페이스를 구현하는 MainActivity 클래스
class SignUpPW : AppCompatActivity(), SignUpView {

    private lateinit var pwInput: EditText
    private lateinit var binding: ActivitySignUpPwBinding // ViewBinding 객체 선언

    // onCreate()는 액티비티가 생성될 때 호출되는 메서드
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpPwBinding.inflate(layoutInflater) // ViewBinding으로 레이아웃 바인딩
        setContentView(binding.root) // 레이아웃을 액티비티에 설정

        pwInput = binding.pwInput

        // 회원가입 버튼 클릭 시 실행될 코드
        binding.nextButton.setOnClickListener {
            signUp() // 회원가입 함수 호출
        }
    }

    private fun getUser(): User {
        val email = intent.getStringExtra("email") ?: ""  // 이메일 값 받기
        val password = binding.pwInput.text.toString()  // 비밀번호 값 가져오기
        return User(email, password, "", "")
    }


    // 회원가입을 처리하는 함수
    private fun signUp() {
        // 비밀번호와 비밀번호 확인이 일치하는지 확인
        if(binding.pwInput.text.toString() != binding.pwCheckInput.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }


        // User 객체 생성
        val user = getUser()

        // 전송할 값 확인 (로그 출력)
        Log.d("SignUp", "Email: ${user.email}, Password: ${user.password}")


        // 2단계: AuthService 객체 생성 후 회원가입 요청
        val authService = AuthService() // AuthService 객체 생성
        authService.setSignUpView(this) // 현재 액티비티를 SignUpView로 설정
        authService.signUp(getUser()) // 회원가입 요청
    }

    // 1단계: SignUpView 인터페이스에서 정의한 onSignUpSuccess 메서드 구현
    override fun onSignUpSuccess() {
        Toast.makeText(this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show() // 회원가입 성공 메시지 표시
    }

    override fun onSignUpFailure() {
        Log.e("SignUpError", "회원가입 요청이 실패했습니다.")

        Toast.makeText(this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show()

        val errorMessage = "서버 응답을 확인하세요"  // 필요시 서버 응답을 받아서 메시지를 표시할 수도 있음
        Toast.makeText(this, "오류: $errorMessage", Toast.LENGTH_LONG).show()
    }

}
