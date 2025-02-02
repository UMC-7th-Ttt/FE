package com.example.fe.mypage

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fe.R
import com.example.fe.bookclub_book.DateConverter
import com.example.fe.databinding.FragmentMypageReviewCalendarBinding
import com.example.fe.mypage.adapter.CalendarRVAdapter
import java.time.LocalDate
import java.time.YearMonth

@RequiresApi(Build.VERSION_CODES.O)
class MyPageReviewCalendarFragment : Fragment() {

    lateinit var binding: FragmentMypageReviewCalendarBinding
    private lateinit var calendarAdapter: CalendarRVAdapter


    private var criteriaDate = LocalDate.now() // 기준 일자 초기화
    private var initialDate: LocalDate = criteriaDate // 초기 날짜 설정


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMypageReviewCalendarBinding.inflate(inflater, container, false)

        initClickListeners()
        setAdapter()

        binding.mypageCalendarReviewSwapBtn.setOnClickListener {
            replaceFragment(MyPageReviewReviewFragment())
        }

        binding.mypageWriteReviewFloatingBtn.setOnClickListener {

            val writeReviewFragment = MyPageWriteReviewFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, writeReviewFragment)
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.vp_fragment_change_container, fragment)
            .addToBackStack(null)
            .commit()
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
        calendarAdapter = CalendarRVAdapter(getSelectedDatePosition(), initialDate.monthValue)
        binding.calendarDateRv.apply {
            layoutManager = GridLayoutManager(requireContext(), DAY_OF_WEEK)
            adapter = calendarAdapter
        }
        setCalendarDate(0)
        // 날짜 클릭 이벤트
//        calendarAdapter.setMyDateClickListener(object: CalendarRVAdapter.MyDateClickListener{
//            override fun onDateClick(selectedDate: LocalDate) {
//                listner.onDateReceived(isStartDate, selectedDate) // 날짜 전달
//                dismiss() // 뒤로가기
//            }
//        })
    }

    private fun setCalendarDate(direct: Long) {
        criteriaDate = criteriaDate.plusMonths(direct)
        // 상단 날짜 세팅
        binding.calendarYearMonthTv.text = DateConverter.getFormattedYearMonth(criteriaDate)
        calendarAdapter.addDateList(dayInMonthArr(criteriaDate))
    }

    // 날짜 생성
    private fun dayInMonthArr(date: LocalDate): ArrayList<LocalDate?> {
        val dateList = ArrayList<LocalDate?>()
        val yearMonth = YearMonth.from(date)

        // 월의 시작일
        val monthFirstDate = date.withDayOfMonth(1)
        // 월 첫 날의 요일 (일요일=0, ... ,토요일=6)
        val dayOfMonthFirstDate = monthFirstDate.dayOfWeek.value % DAY_OF_WEEK
        // 월의 종료일
        val monthLastDate = yearMonth.lengthOfMonth()

        // 요일 순서를 맞추기 위해 인덱스를 조정 (월요일=0, ... ,일요일=6)
        val adjustedFirstDayIndex = if (dayOfMonthFirstDate == 0) 6 else dayOfMonthFirstDate - 1

        // 첫째 주의 빈 칸 추가
        for (i in 0 until adjustedFirstDayIndex) {
            dateList.add(null) // 첫째 주의 빈 칸
        }

        // 실제 날짜 추가
        for (day in 1..monthLastDate) {
            dateList.add(LocalDate.of(date.year, date.monthValue, day))
        }

        // 마지막 주의 빈 칸 추가 (6주로 채우기)
        while (dateList.size < DAY_OF_WEEK * 5) {
            dateList.add(null)
        }

        return dateList
    }

    private fun getSelectedDatePosition(): Int {
        // 월 첫 날의 요일 구하기
        val dayOfWeek = initialDate.withDayOfMonth(1).dayOfWeek.value % DAY_OF_WEEK
        // 초기 날짜의 포지션 계산
        return initialDate.dayOfMonth + dayOfWeek - 1
    }

    companion object {
        const val DAY_OF_WEEK = 7 // 일주일
        const val SUNDAY = 0
        val TODAY: LocalDate = LocalDate.now()
    }
}