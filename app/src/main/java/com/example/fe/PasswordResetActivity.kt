package com.example.fe

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PasswordResetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_password_reset)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val submitButton = findViewById<ImageButton>(R.id.submit_button)
        submitButton.setOnClickListener {
            val email = findViewById<EditText>(R.id.id_input).text.toString()
            sendPasswordResetRequest(email)
        }
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun sendPasswordResetRequest(email: String) {
        if (!isValidEmail(email)) {
            Toast.makeText(this, "유효한 이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // 서버와 통신하여 비밀번호 변경 이메일을 보내는 로직을 구현
        // 예시: 서버 API 호출 (Retrofit, Firebase 등)
        // 이메일을 보내는 API 호출

        // 서버가 이메일을 정상적으로 처리한 후:
        Toast.makeText(this, "비밀번호 변경 메일을 보냈습니다.", Toast.LENGTH_LONG).show()
    }
}
