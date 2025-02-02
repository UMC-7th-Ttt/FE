package com.example.fe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.databinding.ActivityBookclubJoinBinding

class BookclubJoin : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubJoinBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

}