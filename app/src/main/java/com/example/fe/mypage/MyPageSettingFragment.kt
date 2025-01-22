package com.example.fe.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.R
import com.example.fe.databinding.FragmentMypageSettingBinding

class MyPageSettingFragment: Fragment() {

    lateinit var binding:FragmentMypageSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageSettingBinding.inflate(inflater, container, false)

        binding.mypageSettingBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.mypageSettingDetailTv1.setOnClickListener{
            val mypageProfileChangeFragment = MyPageProfileChangeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mypageProfileChangeFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

}