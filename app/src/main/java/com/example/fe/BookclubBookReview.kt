package com.example.fe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.databinding.ActivityBookclubBookReviewBinding

class BookclubBookReview: AppCompatActivity() {

    private lateinit var binding: ActivityBookclubBookReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubBookReviewBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

}