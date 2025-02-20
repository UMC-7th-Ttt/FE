package com.example.fe


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE

object Utils {
    fun getAuthToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val token = sharedPreferences.getString("authToken", null)

        Log.d("Utils", "Auth Token: $token") // 토큰 로그 추가
        return token
    }
}