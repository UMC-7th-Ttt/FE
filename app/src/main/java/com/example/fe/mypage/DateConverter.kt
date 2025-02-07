package com.example.fe.mypage

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
object DateConverter {
    private val formatter = DateTimeFormatter.ofPattern("yyyy.MM") // 원하는 형식으로 설정

    fun getFormattedYearMonth(date: LocalDate): String {
        return date.format(formatter)
    }
}