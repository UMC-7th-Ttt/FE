package com.example.fe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.fe.databinding.ActivitySettingBinding
import com.example.fe.setting.SettingChangeInfoFragment
import com.example.fe.setting.SettingProfileChangeFragment

class Setting: AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.mypageSettingDetailTv1.setOnClickListener {
            val profileChangeFagment = SettingProfileChangeFragment()
            supportFragmentManager.commit {
                replace(R.id.fragment_container, profileChangeFagment)
                addToBackStack(null)
            }
        }

        binding.mypageSettingDetailTv2.setOnClickListener {
            val settingChangeInfoFragment = SettingChangeInfoFragment()
            supportFragmentManager.commit {
                replace(R.id.fragment_container, settingChangeInfoFragment)
                addToBackStack(null)
            }
        }
    }
}