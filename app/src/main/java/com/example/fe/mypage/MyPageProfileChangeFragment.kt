package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentMypageBinding
import com.example.fe.databinding.FragmentMypageProfileChangeBinding

class MyPageProfileChangeFragment:Fragment() {
    lateinit var binding: FragmentMypageProfileChangeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageProfileChangeBinding.inflate(inflater, container, false)

        binding.mypageProfileBackIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}