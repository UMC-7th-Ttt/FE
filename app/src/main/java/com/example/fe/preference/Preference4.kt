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
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityPreference4Binding
import com.example.fe.network.getRetrofit
import com.example.fe.preference.service.BookService
import com.example.fe.preference.service.KeywordRequest
import com.example.fe.preference.service.KeywordService
import com.example.fe.preference.service.KeywordView

class Preference4 : AppCompatActivity(){

    private lateinit var binding: ActivityPreference4Binding


    private lateinit var viewPager: ViewPager2
    private lateinit var descriptionText: TextView
    private val retrofit = getRetrofit()
    private val bookService = retrofit.create(BookService::class.java)

    // 이미지 리소스 목록
    private val images = listOf(
        R.drawable.ic_bookcover_example8, // 첫 번째 이미지
        R.drawable.ic_bookcover_example2, // 두 번째 이미지
        R.drawable.ic_bookcover_example7,  // 세 번째 이미지
        R.drawable.ic_bookcover_example4, // 첫 번째 이미지
        R.drawable.ic_bookcover_example5, // 두 번째 이미지
        R.drawable.ic_bookcover_example6  // 세 번째 이미지
    )

    // 각 이미지에 대응  하는 설명 (줄거리)
    private val descriptions = listOf(
        "p.102\n" + "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다. 네가 방수 모포에 싸여 청소차에 실려간 뒤에. 용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져 나온 뒤에. 어디서나 사원의 불빛이 타고 있었다. 봄에 피는 꽃들 속에, 눈송이들 속에, 날마다 찾아오는 저녁들 속에. 다 쓴 음료수 병에 네가 꽂은 양초 불꽃들이",
        "p.91\n" + "생각해보면 희한할 정도로 많은 사람이 삶에서 스트레스를 우선시한다. 기분 나쁘게 하고 불안하거나 진 빠지게 만드는 일에는 시간을 할애하는 반면, 회복과 사색은 할 일 목록의 맨밑에 두거나 생각조차 하지 않는 경우가 많다. 의도적으로 휴식과 이완을 우선순위에 둔 게 언제인지 기억나는가?",
        "p.14\n"+ "컴퓨터가 인간만큼 안정적인 수준으로 글을 쓸 수 있다고, 혹은 어쩌면 그보다 더 잘 쓸 수 있다고 상상해 보라. 그게 중요한 문제인가? 우리는 그런 발전을 환영할 것인가? 그래야 할까? 이미 AI는 업무 문서와 이메일, 신문과 블로그로 자신의 영역을 넓히고 있다. 걱정스러운 것은 다가올 미래에 단지 인간의 쓰기 능력뿐만 아니라 어떤 일자리 든 여전히 인간에게 유효한 영역으로 남아 있을까 하는 의문이다.",
        "p.104\n"+ "좋은 일이란 오래가는 법이 없구나, 하고 그는 생각했다. 차라리 이게 한낱 꿈이었더라면 얼마나 좋을까. 이 고기는 잡은적도 없고, 지금 이 순간 침대에 신문지를 깔고 혼자 누워 있다면 얼마나 좋을까. ˝하지만 인간은 패배하도록 창조된 게 아니야.˝ 그가 말했다. ˝인간은 파멸당할 수는 있을지 몰라도 패배할 수는 없어.˝",
        "p.229\n" + "집을 보면 그 사람이 어떤 생활을 하는지 추측할 수 있어요. 난 메이바오의 집에 몇 번 가봤을 뿐이지만, 메이바오의 내면은 겉모습처럼 단순하지 않다는 걸 직감적으로 알았어요. 이중생활을 하고 있다고 할까. 가정환경이 좋지 않고 일은 또 너무 바쁘고 힘들고, 이유는 모르지만 뭔가를 피해 숨어 있거나 스스로 형벌을 내리고 있는 것 같았죠.",
        "p.28\n" + "그냥 지금처럼 살아라. 그렇게 살되 어떤 감정조차 책임질 수 없을 만큼 힘든 날, 마음 속이 온통 타인의 감정으로 가득해 당장이라도 터져버릴 것 같은 그런 날. 일부러 나밖에 없는 공간으로 도망가자. 그 조용한 공간에서 자신에게도 이렇게 말할 기회를 주자. “나 안 괜찮아.” 가끔은 남에게 줬던 섬세함을 나에게도 허락하자."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreference4Binding.inflate(layoutInflater)  // 데이터 바인딩 초기화
        setContentView(binding.root)
        binding.nextButton.setOnClickListener {

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
            val intent = Intent(this, MainActivity::class.java) // Replace NextActivity with your next activity
            startActivity(intent)
        }
    }
}