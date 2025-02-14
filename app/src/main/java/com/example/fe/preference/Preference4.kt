package com.example.fe.preference

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.R
import com.example.fe.databinding.ActivityPreference4Binding
import com.example.fe.network.getRetrofit
import com.example.fe.preference.service.Book
import com.example.fe.preference.service.BookService
import com.example.fe.preference.service.KeywordRequest
import com.example.fe.preference.service.KeywordView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.fe.preference.service.BookResponse
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class Preference4 : AppCompatActivity() {

    private lateinit var binding: ActivityPreference4Binding
    private lateinit var viewPager: ViewPager2
    private lateinit var descriptionText: TextView
    private lateinit var sharedPreferences: SharedPreferences

    private val retrofit = getRetrofit()
    private val bookService = retrofit.create(BookService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BookResponseError", "Preference4 화면 열림")
        enableEdgeToEdge()
        binding = ActivityPreference4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        val accessToken = sharedPreferences.getString("accessToken", "")
        if (accessToken.isNullOrEmpty()) {
            Log.e("BookResponseError", "🚨 accessToken 없음. 다시 로그인 필요!")
            runOnUiThread {
                Toast.makeText(this, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
            return
        }


        viewPager = findViewById(R.id.viewPager)
        descriptionText = findViewById(R.id.descriptionText)

        fetchBooks(accessToken)

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.nextButton.setOnClickListener {
            val intent = Intent(this, Preference_complete::class.java)
            startActivity(intent)
        }
    }

    private fun fetchBooks(accessToken: String) {
        val authHeader = "Bearer $accessToken"  // ✅ 올바른 accessToken 사용
        Log.d("BookResponseError", "📡 API 요청 시 사용될 토큰: $authHeader") // << 확인

        bookService.getBestSellers(authHeader).enqueue(object : Callback<BookResponse> {
            override fun onResponse(call: Call<BookResponse>, response: Response<BookResponse>) {
                if (response.isSuccessful) {
                    val rawJson = response.body()
                    Log.d("BookResponse", "📚 서버 응답: $rawJson")
                    response.body()?.result?.books?.let { books -> updateUI(books) }
                } else {
                    Log.e("BookResponseError", "🚨 API 요청 실패: ${response.code()} - ${response.errorBody()?.string()}")
                    if (response.code() == 401) {
                        refreshGoogleToken()
                    }
                }
            }

            override fun onFailure(call: Call<BookResponse>, t: Throwable) {
                Log.e("BookResponseError", "🚨 네트워크 오류", t)
            }
        })
    }


    private fun refreshGoogleToken() {
        val account = GoogleSignIn.getLastSignedInAccount(this)
        account?.serverAuthCode?.let { authCode ->
            Log.d("BookResponseError", "🔄 새로 받은 Google authCode: $authCode")

            val requestBody = JSONObject().apply {
                put("authCode", authCode)
            }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

            val request = Request.Builder()
                .url("http://3.38.209.11:8080/api/google-login")
                .post(requestBody)
                .build()

            OkHttpClient().newCall(request).enqueue(object : okhttp3.Callback {
                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    Log.e("BookResponseError", "🚨 토큰 갱신 실패: ${e.message}")
                }

                override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                    val responseBody = response.body?.string()
                    Log.d("BookResponseError", "🔄 토큰 갱신 응답: $responseBody")

                    val json = JSONObject(responseBody)
                    val newAccessToken = json.getString("accessToken")

                    // ✅ 새 accessToken 저장
                    sharedPreferences.edit()
                        .putString("authToken", newAccessToken)
                        .apply()

                    Log.d("BookResponseError", "✔️ accessToken 갱신 완료: $newAccessToken")
                }
            })
        } ?: Log.e("BookResponseError", "🚨 새 토큰을 가져올 수 없음! 재로그인 필요")
    }

    private fun updateUI(books: List<Book>) {
        val images = books.map { it.cover }
        val descriptions = books.map { it.mainSentences }

        val adapter = ViewPagerAdapter(images)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                descriptionText.text = descriptions[position]
            }
        })

        descriptionText.text = descriptions.firstOrNull() ?: "책 정보가 없습니다."
    }
}
