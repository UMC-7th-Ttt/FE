package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemCalendarDateBinding
import java.time.LocalDate

class CalendarRVAdapter(private val selectedDatePosition: Int, private val selectedMonth: Int) : RecyclerView.Adapter<CalendarRVAdapter.ViewHolder>() {

    private var dateList = listOf<LocalDate?>() // 달력에 표시될 날짜 목록
    private var selectedItemPosition = -1 // 달이 넘어가더라도 선택한 날짜는 유일하게 표시해주기 위함
    private lateinit var mItemClickListener: MyDateClickListener

    private lateinit var context: Context

    interface MyDateClickListener {
        fun onDateClick(selectedDate: LocalDate)
    }

    fun setMyDateClickListener(itemClickListener: MyDateClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    fun addDateList(dateList: List<LocalDate?>) {
        this.dateList = dateList
        this.selectedItemPosition = if (dateList[10]!!.monthValue == selectedMonth) selectedDatePosition else -1
        notifyDataSetChanged()
    }

    // 보여지는 화면 설정
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCalendarDateBinding = ItemCalendarDateBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false
        )
        context = viewGroup.context
        return ViewHolder(binding)
    }

    // 내부 데이터 설정
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (dateList[position] == null) { // 날짜 데이터가 없을 경우 캘린더에 표시하지 않음
            holder.dateText.text = null
            return
        }

        // 날짜의 date만 표시
        holder.dateText.text = dateList[position]!!.dayOfMonth.toString()
//
//        // 날짜 클릭 이벤트
//        holder.bg.setOnClickListener {
//            notifyItemChanged(selectedItemPosition) // 이전에 선택한 아이템 notify
//            selectedItemPosition = position // 선택한 날짜 position 업데이트
//            notifyItemChanged(selectedItemPosition) // 새로 선택한 아이템 notify
//            mItemClickListener.onDateClick(dateList[selectedItemPosition]!!) // 클릭 이벤트 처리
//        }
    }

    override fun getItemCount(): Int = dateList.size

    inner class ViewHolder(val binding: ItemCalendarDateBinding): RecyclerView.ViewHolder(binding.root){
        val bg: LinearLayout = binding.itemCalendarDateBg
        var dateText: TextView = binding.itemCalendarDateTv
    }
}