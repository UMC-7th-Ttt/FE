package com.example.fe.preference

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.MainActivity
import com.example.fe.login.Login
import com.example.fe.R

class Preference_complete : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 먼저 setContentView() 호출
        setContentView(R.layout.activity_sign_up_complete)

        // 이후 findViewById 호출
        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            // NextActivity로 이동
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        enableEdgeToEdge()

        // 윈도우 인셋 적용
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
