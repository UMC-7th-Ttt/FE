package com.example.fe.bookclub_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentBookclubBookReviewBinding

class BookclubBookReviewFragment:Fragment() {
    lateinit var binding: FragmentBookclubBookReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookReviewBinding.inflate(inflater, container, false)

        return binding.root
    }
}