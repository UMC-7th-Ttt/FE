package com.example.fe.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentSettingProfileChangeBinding

class SettingProfileChangeFragment:Fragment() {
    lateinit var binding: FragmentSettingProfileChangeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingProfileChangeBinding.inflate(inflater, container, false)

        binding.mypageProfileBackIv.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}