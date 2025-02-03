package com.example.fe.signup
import android.content.Intent
import android.os.Bundle
import android.widget.CheckBox
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R

//import com.example.fe.databinding.ActivityTermsofUseBinding

class TermsofUse : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_termsof_use)

        // 1. Back 버튼 처리
        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료 후 이전 페이지로 돌아감
        }

        // 2. CheckBox 동기화 로직
        val allCheckBox: CheckBox = findViewById(R.id.allcheckBox)
        val checkBox1: CheckBox = findViewById(R.id.checkBox1)
        val checkBox2: CheckBox = findViewById(R.id.checkBox2)
        val checkBox3: CheckBox = findViewById(R.id.checkBox3)
        val checkBox4: CheckBox = findViewById(R.id.checkBox4)

        val individualCheckBoxes = listOf(checkBox1, checkBox2, checkBox3, checkBox4)

        // allCheckBox를 클릭하면 아래 CheckBox1~4의 상태가 변경됨
        allCheckBox.setOnCheckedChangeListener { _, isChecked ->
            individualCheckBoxes.forEach { it.isChecked = isChecked }
        }

        // CheckBox1~4 중 하나라도 해제되면 allCheckBox가 체크 해제됨
        // 모두 체크되면 allCheckBox도 체크됨
        individualCheckBoxes.forEach { checkBox ->
            checkBox.setOnCheckedChangeListener { _, _ ->
                allCheckBox.isChecked = individualCheckBoxes.all { it.isChecked }
            }
        }

        // next_button 클릭 시 다음 페이지로 이동
        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            // NextActivity로 이동
            val intent = Intent(this, SignUpID::class.java)
            startActivity(intent)
        }

    }
}

//        // 3. API 연결 준비 (선택 상태 확인 로직)
//        findViewById<ImageButton>(R.id.some_submit_button)?.setOnClickListener {
//            // 선택된 데이터 준비
//            val termsData = mapOf(
//                "all" to allCheckBox.isChecked,
//                "term1" to checkBox1.isChecked,
//                "term2" to checkBox2.isChecked,
//                "term3" to checkBox3.isChecked,
//                "term4" to checkBox4.isChecked
//            )
//
//            // 나중에 API에 데이터 넘길 때 사용 가능
//            sendTermsToServer(termsData)
//        }
//    }
//
//    private fun sendTermsToServer(data: Map<String, Boolean>) {
//        // 나중에 Retrofit을 사용해 서버로 전송
//        // 예제: Toast로 데이터 확인
//        Toast.makeText(this, "데이터 전송: $data", Toast.LENGTH_SHORT).show()
//
//        // Retrofit/Volley 코드 작성 필요
//    }
//}
