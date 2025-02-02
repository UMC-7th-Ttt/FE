package com.example.fe.Preference

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.R

class Preference3 : AppCompatActivity() {

    private var selectedCount = 0
    private lateinit var nextButton: ImageButton
    private lateinit var optionViews: List<TextView> // Holds all the TextViews representing options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preference3)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        nextButton = findViewById(R.id.next_button) // Initialize nextButton here

        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료 후 이전 페이지로 돌아감
        }

        // Initialize the next button
        nextButton = findViewById(R.id.next_button)
        nextButton.isEnabled = false  // Start with the next button disabled

        // Initialize the options list (TextViews for the options)
        optionViews = listOf(
            findViewById(R.id.option1),
            findViewById(R.id.option2),
            findViewById(R.id.option3),
            findViewById(R.id.option4),
            findViewById(R.id.option5),
            findViewById(R.id.option6),
            findViewById(R.id.option7),
            findViewById(R.id.option8)
        )

        // Set click listeners on each TextView
        optionViews.forEach { option ->
            option.setOnClickListener {
                toggleSelection(option)
            }
        }
        nextButton.setOnClickListener {
            // Start the next activity
            val intent = Intent(this, Preference4::class.java)
            startActivity(intent)
        }
    }

    private fun toggleSelection(option: TextView) {
        // Toggle the background to indicate selection
        if (option.isSelected) {
            option.isSelected = false
            option.setBackgroundResource(R.color.white_20) // Unselected state
            selectedCount--
        } else {
            option.isSelected = true
            option.setBackgroundResource(R.color.primary_50) // Selected state
            option.setTextColor(resources.getColor(R.color.black))
            selectedCount++
        }

        // Enable or disable the next button based on the selected count
        nextButton.isEnabled = selectedCount >= 3
    }
}
