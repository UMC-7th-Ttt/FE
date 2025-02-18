package com.example.fe.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.fe.R
import com.example.fe.databinding.ActivitySettingBinding
import com.example.fe.mypage.MyPageScrapDetail

class Setting: AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.mypageSettingDetailTv1.setOnClickListener {
            val intent = Intent(this, ChangeProfile::class.java)
            startActivity(intent)
        }

        binding.mypageSettingDetailTv2.setOnClickListener {
            val intent = Intent(this, ChangeInfo::class.java)
            startActivity(intent)
        }
    }
}