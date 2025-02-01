package com.example.fe.mypage

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageWriteReviewBinding

class MyPageWriteReviewFragment:Fragment() {
    lateinit var binding: FragmentMypageWriteReviewBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageWriteReviewBinding.inflate(inflater, container, false)

        binding.mypageWriteReviewBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}