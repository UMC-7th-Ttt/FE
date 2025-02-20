package com.example.fe.preference

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityPreference4Binding
import com.example.fe.network.getRetrofit
import com.example.fe.preference.service.BookService
import com.example.fe.preference.service.KeywordRequest
import com.example.fe.preference.service.KeywordService
import com.example.fe.preference.service.KeywordView
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class Preference4 : AppCompatActivity() {

    private lateinit var binding: ActivityPreference4Binding
    private lateinit var viewPager: ViewPager2
    private lateinit var descriptionText: TextView
    private val retrofit = getRetrofit()
    private val bookService = retrofit.create(BookService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPreference4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = findViewById(R.id.viewPager)
        descriptionText = findViewById(R.id.descriptionText)

        fetchBooks()

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener { finish() }

        // ViewPager 설정
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin) // XML에서 정의
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset) // XML에서 정의

        viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3

            val screenWidth = resources.displayMetrics.widthPixels
            val pageWidth = (screenWidth * 0.5).toInt() // 각 페이지의 크기를 화면의 70%로 설정
            val offsetPx = (screenWidth - pageWidth) / 2 // 좌우 여백 계산

            setPadding(offsetPx, 0, offsetPx, 0)

            // RecyclerView에 clip 속성 적용 (post 사용)
            post {
                val recyclerView = getChildAt(0) as? RecyclerView
                recyclerView?.apply {
                    clipToPadding = false
                    clipChildren = false
                    overScrollMode = View.OVER_SCROLL_NEVER
                }
            }

            // 페이지 전환 애니메이션 추가
            setPageTransformer { page, position ->
                val scaleFactor = 0.85f + (1 - Math.abs(position)) * 0.15f
                page.scaleX = scaleFactor
                page.scaleY = scaleFactor
                page.alpha = 0.7f + (1 - Math.abs(position)) * 0.3f
            }
        }

        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            val intent = Intent(this, Preference_complete::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /**
     * 서버에서 베스트셀러 책 목록을 가져오는 함수
     */
    private fun fetchBooks() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val authToken = sharedPreferences.getString("authToken", null)

        if (authToken != null) {
            val request = Request.Builder()
                .url("http://3.38.209.11:8080/api/books/bestsellers")
                .header("Authorization", "Bearer $authToken")
                .get()
                .build()

            val client = OkHttpClient()
            client.newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("API_TEST", "API 요청 실패: ${e.message}")
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val responseBody = response.body?.string()
                    Log.d("API_TEST", "API 응답: $responseBody")

                    responseBody?.let { json ->
                        runOnUiThread { updateUIWithBooks(json) }
                    }
                }
            })
        } else {
            Log.d("TOKEN_TEST", "저장된 토큰 없음. 로그인 필요")
        }
    }

    /**
     * API 응답 데이터를 기반으로 ViewPager 및 설명을 업데이트하는 함수
     */
    private fun updateUIWithBooks(json: String) {
        try {
            val jsonObject = JSONObject(json)
            val booksArray = jsonObject.getJSONObject("result").getJSONArray("books")

            val images = mutableListOf<String>()
            val descriptions = mutableListOf<String>()

            for (i in 0 until booksArray.length()) {
                val book = booksArray.getJSONObject(i)
                images.add(book.getString("cover")) // 책 표지 이미지 URL
                descriptions.add(book.getString("mainSentences")) // 책 줄거리
            }

            // ViewPager 업데이트
            val adapter = ViewPagerAdapter(images)
            viewPager.adapter = adapter

            // 페이지 변경 시 설명 업데이트
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    descriptionText.text = descriptions[position]
                }
            })

            // 첫 번째 페이지 설명 초기 설정
            if (descriptions.isNotEmpty()) {
                descriptionText.text = descriptions[0]
            }
        } catch (e: JSONException) {
            Log.e("JSON_ERROR", "JSON 파싱 오류: ${e.message}")
        }
    }
}

