package com.example.fe.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentSettingAlarmBinding
import com.example.fe.databinding.FragmentSettingChangeInfoBinding

class SettingAlarmFragment:Fragment() {
    lateinit var binding: FragmentSettingAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingAlarmBinding.inflate(inflater, container, false)

        binding.settingAlarmBackBtn.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        return binding.root
    }
}