package com.example.fe.mypage

data class CalendarItem(val date: String, val imageResId: Int? = null)

data class MonthData(val month: String, val year: Int, val days: List<CalendarItem>)

