package com.example.fe.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fe.JohnRetrofitClient
import com.example.fe.Review.ReviewActivity
import com.example.fe.bookclub_book.server.CalendarResponse
import com.example.fe.databinding.FragmentMypageReviewCalendarBinding
import com.example.fe.mypage.adapter.CalendarRVAdapter
import com.example.fe.mypage.server.MyPageRetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class MyPageReviewCalendarFragment : Fragment() {

    lateinit var binding: FragmentMypageReviewCalendarBinding
    private lateinit var calendarAdapter: CalendarRVAdapter

    private var criteriaDate = LocalDate.now() // 기준 일자 초기화
    private var initialDate: LocalDate = criteriaDate // 초기 날짜 설정

    private val reviewList = mutableListOf<CalendarResponse.Result.Review>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageReviewCalendarBinding.inflate(inflater, container, false)

        initClickListeners()
        setAdapter()

        binding.mypageWriteReviewFloatingBtn.setOnClickListener {
            context?.let { ctx ->
                val intent = Intent(ctx, ReviewActivity::class.java)
                startActivity(intent)
            }
        }

        binding.changeBtn.setOnClickListener {
            val intent = Intent(context,MyPageReviewList::class.java)
            startActivity(intent)
        }

        fetchReviews(criteriaDate.year, criteriaDate.monthValue) // 리뷰 데이터를 가져옵니다

        return binding.root
    }

    private fun initClickListeners() {
        /* 화살표 눌러서 월 이동 */
        binding.calendarPreviousMonthIv.setOnClickListener { // 이전 달
            setCalendarDate(-1)
        }
        binding.calendarNextMonthIv.setOnClickListener { // 다음 달
            setCalendarDate(+1)
        }
    }

    private fun setAdapter() {
        // 어댑터 초기화
        calendarAdapter = CalendarRVAdapter(getSelectedDatePosition(), initialDate.monthValue, reviewList)
        binding.calendarDateRv.apply {
            layoutManager = GridLayoutManager(requireContext(), DAY_OF_WEEK)
            adapter = calendarAdapter
        }
        setCalendarDate(0)
    }

    private fun setCalendarDate(direct: Long) {
        criteriaDate = criteriaDate.plusMonths(direct)
        // 상단 날짜 세팅
        binding.calendarYearMonthTv.text = DateConverter.getFormattedYearMonth(criteriaDate)
        calendarAdapter.setSelectedMonth(criteriaDate.monthValue)
        calendarAdapter.addDateList(dayInMonthArr(criteriaDate))
        fetchReviews(criteriaDate.year, criteriaDate.monthValue)
    }

    // 날짜 생성
    private fun dayInMonthArr(date: LocalDate): ArrayList<LocalDate?> {
        var dateList = ArrayList<LocalDate?>()
        val yearMonth = YearMonth.from(date)

        // 월의 시작일
        val monthFirstDate = date.withDayOfMonth(1)
        // 월 첫 날의 요일 (월요일=1, ... ,일요일=7)
        val dayOfMonthFirstDate = monthFirstDate.dayOfWeek.value
        // 월의 종료일
        val monthLastDate = yearMonth.lengthOfMonth()

        // 첫째 주의 빈 칸 추가 (이전 달의 날짜로 채우기)
        val prevMonth = date.minusMonths(1)
        val prevYearMonth = YearMonth.from(prevMonth)
        val prevMonthDays = prevYearMonth.lengthOfMonth()
        for (i in dayOfMonthFirstDate - 1 downTo 1) {
            dateList.add(LocalDate.of(prevMonth.year, prevMonth.monthValue, prevMonthDays - i ))
        }

        // 실제 날짜 추가
        for (day in 1..monthLastDate) {
            dateList.add(LocalDate.of(date.year, date.monthValue, day))
        }

        // 마지막 주의 빈 칸 추가 (다음 달의 날짜로 채우기)
        val nextMonth = date.plusMonths(1)
        for (i in 1..(DAY_OF_WEEK * 6 - dateList.size)) {
            dateList.add(LocalDate.of(nextMonth.year, nextMonth.monthValue, i))
        }

        return dateList
    }

    private fun getSelectedDatePosition(): Int {
        // 월 첫 날의 요일 구하기
        val dayOfWeek = initialDate.withDayOfMonth(1).dayOfWeek.value % DAY_OF_WEEK
        // 초기 날짜의 포지션 계산
        return initialDate.dayOfMonth + dayOfWeek - 1
    }

    private fun fetchReviews(year: Int, month: Int) {
        val api = JohnRetrofitClient.getClient(requireContext()).create(MyPageRetrofitInterface::class.java)
        api.getCalendarReviews(year, month).enqueue(object : Callback<CalendarResponse> {
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<CalendarResponse>, response: Response<CalendarResponse>) {
                if (response.isSuccessful) {
                    response.body()?.result?.reviewList?.let {
                        reviewList.addAll(it)
                        Log.d("MyPageReviewCalendarFragment", "Fetched reviews: $it")
                        calendarAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<CalendarResponse>, t: Throwable) {
                // 오류 처리
            }
        })
    }

    companion object {
        const val DAY_OF_WEEK = 7
    }
}