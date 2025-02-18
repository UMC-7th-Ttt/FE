package com.example.fe.setting

import android.content.Context
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityAlarmBinding

class Alarm : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var isActivityToggleActive: Boolean = false
    private var isMarketingToggleActive: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load toggle states from SharedPreferences
        val sharedPreferences = getSharedPreferences("TogglePreferences", Context.MODE_PRIVATE)
        isActivityToggleActive = sharedPreferences.getBoolean("activity_toggle", false)
        isMarketingToggleActive = sharedPreferences.getBoolean("marketing_toggle", true)

        // Set initial states
        setToggleState(binding.activityToggle, isActivityToggleActive)
        setToggleState(binding.marketingToggle, isMarketingToggleActive)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.activityToggle.setOnClickListener {
            isActivityToggleActive = !isActivityToggleActive
            setToggleState(binding.activityToggle, isActivityToggleActive)
            saveToggleState("activity_toggle", isActivityToggleActive)
        }

        binding.marketingToggle.setOnClickListener {
            isMarketingToggleActive = !isMarketingToggleActive
            setToggleState(binding.marketingToggle, isMarketingToggleActive)
            saveToggleState("marketing_toggle", isMarketingToggleActive)
        }
    }

    private fun setToggleState(imageView: ImageView, isActive: Boolean) {
        if (isActive) {
            imageView.setImageResource(R.drawable.ic_toggle_activate)
        } else {
            imageView.setImageResource(R.drawable.ic_toggle_deactivate)
        }
    }

    private fun saveToggleState(key: String, state: Boolean) {
        val sharedPreferences = getSharedPreferences("TogglePreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, state)
        editor.apply()
    }
}