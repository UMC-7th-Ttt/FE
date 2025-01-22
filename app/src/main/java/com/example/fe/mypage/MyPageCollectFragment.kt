package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentMypageCompleteBinding

class MyPageCollectFragment: Fragment() {
    lateinit var binding: FragmentMypageCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root
    }
}