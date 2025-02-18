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

class Preference : AppCompatActivity() {

    private var selectedCount = 0
    private lateinit var nextButton: ImageButton
    private lateinit var optionViews: List<TextView> // 모든 옵션을 나타내는 TextView 목록
    private val selectedOptions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preference)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val backButton: ImageButton = findViewById(R.id.back_button)
        nextButton = findViewById(R.id.next_button) // nextButton 초기화

        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료 후 이전 페이지로 돌아감
        }

        // nextButton 초기화
        nextButton = findViewById(R.id.next_button)
        nextButton.isEnabled = false  // 시작 시 nextButton 비활성화

        // 옵션 목록 초기화 (옵션을 위한 TextView들)
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

        // 각 TextView에 클릭 리스너 설정
        optionViews.forEach { option ->
            option.setOnClickListener {
                toggleSelection(option)
            }
        }
        nextButton.setOnClickListener {
            // 다음 액티비티 시작
            val intent = Intent(this, Preference2::class.java)
            intent.putStringArrayListExtra("selectedOptions", ArrayList(selectedOptions))
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
            selectedOptions.remove(option.text.toString()) // 리스트에서 제거
        } else {
            // 선택되지 않은 상태라면, 배경을 선택된 상태로 변경하고, 글자 색상 검정색으로 변경
            option.setBackgroundResource(selectedBackground)
            option.setTextColor(resources.getColor(R.color.black)) // 글자 색상 검정색
            option.tag = "selected" // 상태 변경
            selectedCount++
            selectedOptions.add(option.text.toString()) // 리스트에 추가
        }

        // 선택된 아이템의 개수가 3개 이상이면 다음 버튼 활성화
        nextButton.isEnabled = selectedCount >= 3
    }

}
