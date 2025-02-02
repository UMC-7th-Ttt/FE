package com.example.fe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.databinding.ActivityMainBinding
import com.example.fe.databinding.ActivityMypageWriteReviewBinding

class MyPageWriteReview:AppCompatActivity() {
    private lateinit var binding: ActivityMypageWriteReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMypageWriteReviewBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}