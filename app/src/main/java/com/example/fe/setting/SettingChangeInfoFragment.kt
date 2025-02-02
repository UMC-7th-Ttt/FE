package com.example.fe.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.R
import com.example.fe.databinding.FragmentSettingChangeInfoBinding

class SettingChangeInfoFragment: Fragment() {

    lateinit var binding: FragmentSettingChangeInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingChangeInfoBinding.inflate(inflater, container, false)

        binding.settingChangeInfoBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.alarmTv.setOnClickListener{
            val settingAlarmFragment = SettingAlarmFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, settingAlarmFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }
}