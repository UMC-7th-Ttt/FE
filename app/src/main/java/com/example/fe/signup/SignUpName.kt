package com.example.fe.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.fe.databinding.ActivitySignUpNameBinding
import com.example.fe.signup.service.NicknameService
import com.example.fe.signup.service.NicknameView

class SignUpName : AppCompatActivity(), NicknameView {
    private lateinit var binding: ActivitySignUpNameBinding
    private lateinit var authService: NicknameService  // 서비스 선언

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("id", -1)
        Log.d("SignUpName", "회원가입 ID: $id")
        authService = NicknameService()  // 초기화
        authService.setNicknameView(this)  // View 연결

        binding.sendButton.setOnClickListener {
            val nickname = binding.sendButton.text.toString()
            authService.nickName(nickname)  // 닉네임 중복 확인 요청
        }

        // 다음 페이지로 이동
        binding.nextButton.setOnClickListener {
            val intent = Intent(this, SignUpComplete::class.java) // SignUpComplete로 이동
            startActivity(intent)
        }

        // 사진 넣기 기능 (갤러리에서 선택)
        binding.pic2.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

    }

    // 갤러리에서 선택한 이미지 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            binding.pic1.setImageURI(selectedImageUri)  // 선택한 이미지 설정
        }
    }


    override fun nicknameCheckSuccess() {
        Toast.makeText(this, "사용 가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
    }

    override fun nicknameCheckFailure() {
        Toast.makeText(this, "이미 사용 중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
    }
}
