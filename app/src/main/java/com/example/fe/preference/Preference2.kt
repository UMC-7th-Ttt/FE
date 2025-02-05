package com.example.fe.preference

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.R

class Preference2 : AppCompatActivity() {

    private var selectedCount = 0
    private lateinit var nextButton: ImageButton
    private lateinit var optionViews: List<TextView> // Holds all the TextViews representing options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preference2)

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
            findViewById(R.id.option8),
            findViewById(R.id.option9),
            findViewById(R.id.option10),
            findViewById(R.id.option11),
            findViewById(R.id.option12),
            findViewById(R.id.option13),
            findViewById(R.id.option14),
            findViewById(R.id.option15)
        )

        // Set click listeners on each TextView
        optionViews.forEach { option ->
            option.setOnClickListener {
                toggleSelection(option)
            }
        }
        nextButton.setOnClickListener {
            // Start the next activity
            val intent = Intent(this, Preference3::class.java)
            startActivity(intent)
        }
    }

    private fun toggleSelection(option: TextView) {
        // 기본 배경 색상과 텍스트 색상 저장
        val defaultBackground = R.drawable.background_preference_unselected // 기본 배경
        val selectedBackground = R.drawable.background_preference_selected // 선택된 배경
        val defaultTextColor = resources.getColor(R.color.white) // 기본 텍스트 색상

        // 선택된 상태를 확인하고, 선택 여부에 따라 배경 색상 및 글자 색상 변경
        if (option.tag == "selected") {
            // 이미 선택된 상태라면, 배경을 기본 상태로 변경하고, 글자 색상 원래대로 복원
            option.setBackgroundResource(defaultBackground)
            option.setTextColor(defaultTextColor)
            option.tag = "unselected" // 상태 변경
            selectedCount--
        } else {
            // 선택되지 않은 상태라면, 배경을 선택된 상태로 변경하고, 글자 색상 검정색으로 변경
            option.setBackgroundResource(selectedBackground)
            option.setTextColor(resources.getColor(R.color.black)) // 글자 색상 검정색
            option.tag = "selected" // 상태 변경
            selectedCount++
        }

        // 선택된 아이템의 개수가 3개 이상이면 다음 버튼 활성화
        nextButton.isEnabled = selectedCount >= 3
    }
}
