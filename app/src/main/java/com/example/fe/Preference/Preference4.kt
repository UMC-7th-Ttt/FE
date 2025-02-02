package com.example.fe.Preference

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.R

class Preference4 : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var descriptionText: TextView

    // 이미지 리소스 목록
    private val images = listOf(
        R.drawable.ic_bookcover_exmaple2, // 첫 번째 이미지
        R.drawable.ic_bookcover_exmaple2, // 두 번째 이미지
        R.drawable.ic_bookcover_exmaple2  // 세 번째 이미지
    )

    // 각 이미지에 대응하는 설명 (줄거리)
    private val descriptions = listOf(
        "P. 102\n" + "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다." + "네가 방수 모포에 싸여 청소차에 실려간 뒤에.용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에.어디서나 사원의 불빛이 타고 있었다.봄에 피는 꽃들 속에, 눈송이들 속에,날마다 찾아오는 저녁들 속에. \n" + "다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이",
        "P. 45\n" + "코드 오류는 절대 안된다" + "네가 방수 모포에 싸여 청소차에 실려간 뒤에.용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에.어디서나 사원의 불빛이 타고 있었다.봄에 피는 꽃들 속에, 눈송이들 속에,날마다 찾아오는 저녁들 속에. \n" + "다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이",
        "P. 102\n" + "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다." + "네가 방수 모포에 싸여 청소차에 실려간 뒤에.용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에.어디서나 사원의 불빛이 타고 있었다.봄에 피는 꽃들 속에, 눈송이들 속에,날마다 찾아오는 저녁들 속에. \n" + "다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_preference4)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ViewPager와 TextView 연결
        viewPager = findViewById(R.id.viewPager)
        descriptionText = findViewById(R.id.descriptionText)

        // 어댑터 설정
        val adapter = ViewPagerAdapter(images)
        viewPager.adapter = adapter

        // 페이지 변경 시 descriptionText 업데이트
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                descriptionText.text = descriptions[position]
            }
        })

        // 초기 텍스트 설정
        descriptionText.text = descriptions[0]

        // Initialize views after setContentView
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료 후 이전 페이지로 돌아감
        }

        // 다음 버튼 클릭 이벤트 처리
        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            val intent = Intent(this, Preference_complete::class.java) // Replace NextActivity with your next activity
            startActivity(intent)
        }
    }
}