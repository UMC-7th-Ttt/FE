package com.example.fe.bookclub_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentBookclubBookRegisterBinding

class BookclubBookRegisterFragment:Fragment() {
    private lateinit var binding: FragmentBookclubBookRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }
}