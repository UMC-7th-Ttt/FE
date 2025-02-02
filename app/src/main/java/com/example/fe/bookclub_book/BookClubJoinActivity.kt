package com.example.fe.bookclub_book

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityBookClubJoinBinding

class BookClubJoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookClubJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookClubJoinBinding.inflate(layoutInflater)

        binding.backBtn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }

}