package com.example.fe.mypage

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import java.util.Calendar

class MyPageReviewFragment : Fragment() {
    private lateinit var calendarRecyclerView: RecyclerView
    private lateinit var calendarAdapter: UnifiedCalendarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage_review, container, false)

        calendarRecyclerView = view.findViewById(R.id.mypage_calendar_rv)

        setupCalendarRecyclerView()


        return view
    }

    private fun setupCalendarRecyclerView() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        // 월 데이터 생성
        val monthData = generateMonthData(currentMonth, currentYear)
        calendarAdapter = UnifiedCalendarAdapter(listOf(monthData))
        calendarRecyclerView.layoutManager = LinearLayoutManager(context)
        calendarRecyclerView.adapter = calendarAdapter
    }

    private fun generateMonthData(month: Int, year: Int): MonthData {
        val days = mutableListOf<CalendarItem>()
        val daysInMonth = when (month) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            4, 6, 9, 11 -> 30
            2 -> if (isLeapYear(year)) 29 else 28
            else -> 0
        }

        val firstDayOfMonth = Calendar.getInstance().apply {
            set(year, month - 1, 1)
        }.get(Calendar.DAY_OF_WEEK)

        for (i in 1 until firstDayOfMonth) {
            days.add(CalendarItem("", null))
        }

        for (day in 1..daysInMonth) {
            val imageResId = when (day) {
                1 -> R.drawable.img_review_book1
                15 -> R.drawable.img_review_book2
                30 -> R.drawable.img_review_book3
                else -> null
            }
            days.add(CalendarItem(day.toString(), imageResId))
        }

        while (days.size % 7 != 0) {
            days.add(CalendarItem("", null))
        }

        return MonthData(getMonthName(month), year, days)
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> ". 01"
            2 -> ". 02"
            3 -> ". 03"
            4 -> ". 04"
            5 -> ". 05"
            6 -> ". 06"
            7 -> ". 07"
            8 -> ". 08"
            9 -> ". 09"
            10 -> ". 10"
            11 -> ". 11"
            12 -> ". 12"
            else -> ""
        }
    }
}