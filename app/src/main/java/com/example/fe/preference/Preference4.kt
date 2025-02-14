package com.example.fe.preference

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.R
import com.example.fe.databinding.ActivityPreference4Binding
import com.example.fe.network.getRetrofit
import com.example.fe.preference.service.BookService
import com.example.fe.preference.service.KeywordRequest
import com.example.fe.preference.service.KeywordService
import com.example.fe.preference.service.KeywordView

class Preference4 : AppCompatActivity(), KeywordView {

    private lateinit var binding: ActivityPreference4Binding


    private lateinit var viewPager: ViewPager2
    private lateinit var descriptionText: TextView
    private val retrofit = getRetrofit()
    private val bookService = retrofit.create(BookService::class.java)

    // 이미지 리소스 목록
    private val images = listOf(
        R.drawable.ic_bookcover_exmaple2, // 첫 번째 이미지
        R.drawable.ic_bookcover_exmaple2, // 두 번째 이미지
        R.drawable.ic_bookcover_exmaple2  // 세 번째 이미지
    )

    // 각 이미지에 대응  하는 설명 (줄거리)
    private val descriptions = listOf(
        "P. 102\n" + "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다." + "네가 방수 모포에 싸여 청소차에 실려간 뒤에.용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에.어디서나 사원의 불빛이 타고 있었다.봄에 피는 꽃들 속에, 눈송이들 속에,날마다 찾아오는 저녁들 속에. \n" + "다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이",
        "P. 45\n" + "코드 오류는 절대 안된다" + "네가 방수 모포에 싸여 청소차에 실려간 뒤에.용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에.어디서나 사원의 불빛이 타고 있었다.봄에 피는 꽃들 속에, 눈송이들 속에,날마다 찾아오는 저녁들 속에. \n" + "다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이",
        "P. 102\n" + "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다." + "네가 방수 모포에 싸여 청소차에 실려간 뒤에.용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에.어디서나 사원의 불빛이 타고 있었다.봄에 피는 꽃들 속에, 눈송이들 속에,날마다 찾아오는 저녁들 속에. \n" + "다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreference4Binding.inflate(layoutInflater)  // 데이터 바인딩 초기화
        setContentView(binding.root)

        // 버튼 클릭 시 회원가입 함수 호출
        binding.nextButton.setOnClickListener {
            //keyword()
        }

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
    //여기 부분 입력이 아니라 버튼이라 수정필요함
//    private fun getUser(): KeywordRequest {
//        // 각 카테고리 리스트를 binding을 통해 입력받습니다.
//        val preferCategory1: List<String> = binding.preferCategory1Et.text.toString().split(",")  // 쉼표로 구분된 문자열을 List로 변환
//        val preferCategory2: List<String> = binding.preferCategory2Et.text.toString().split(",")
//        val preferCategory3: List<String> = binding.preferCategory3Et.text.toString().split(",")
//
//        // preferBookId는 Integer로 받기
//        val preferBookId: Int = binding.preferBookIdEt.text.toString().toIntOrNull() ?: 0  // 입력 값이 없거나 잘못된 경우 0으로 설정
//
//        // KeywordRequest 객체 반환
//        return KeywordRequest(
//            preferCategory1 = preferCategory1,
//            preferCategory2 = preferCategory2,
//            preferCategory3 = preferCategory3,
//            preferBookId = preferBookId
//        )
//    }
//    private fun keyword(){
//        val authService = KeywordService()
//        authService.setKeywordView(this)  // View 연결
//        authService.keyword(getUser())  // 회원가입 요청
//    }

    override fun onKeywordSuccess() {
        Toast.makeText(this, "회원가입에 성공했습니다", Toast.LENGTH_SHORT).show()
    }

    // 회원가입 실패 시 처리 (미구현 상태)
    override fun onKeywordFailure(error: String) {
        Toast.makeText(this, "회원가입에 실패했습니다", Toast.LENGTH_SHORT).show()
    }
}