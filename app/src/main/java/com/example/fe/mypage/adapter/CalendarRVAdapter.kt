package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.bookclub_book.dataclass.CalendarResponse
import com.example.fe.databinding.ItemCalendarDateBinding
import java.time.LocalDate

class CalendarRVAdapter(
    private val selectedDatePosition: Int,
    private var selectedMonth: Int,
    private val reviewList: List<CalendarResponse.Result.Review>
) : RecyclerView.Adapter<CalendarRVAdapter.ViewHolder>() {

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

    fun setSelectedMonth(month: Int) {
        this.selectedMonth = month
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
            holder.coverImage.visibility = ImageView.GONE
            return
        }

        // 날짜의 date만 표시
        holder.dateText.text = dateList[position]!!.dayOfMonth.toString()

        // 리뷰가 있는 날짜의 경우 커버 이미지 표시
        val date = dateList[position]
        val review = reviewList.find { it.writeDate == date.toString() }
        if (review != null) {
            holder.coverImage.visibility = ImageView.VISIBLE
            holder.dateText.visibility = TextView.GONE
            Glide.with(context)
                .load(review.cover)
                .into(holder.coverImage)
        } else {
            holder.coverImage.visibility = ImageView.GONE
            holder.dateText.visibility = TextView.VISIBLE
        }

        // 이전 달과 다음 달의 날짜를 회색으로 표시
        if (date != null) {
            if (date.monthValue != selectedMonth) {
                holder.dateText.setTextColor(android.graphics.Color.parseColor("#808080"))
            } else {
                holder.dateText.setTextColor(android.graphics.Color.parseColor("#F2F2F2"))
            }
        }
    }

    override fun getItemCount(): Int = dateList.size

    inner class ViewHolder(val binding: ItemCalendarDateBinding): RecyclerView.ViewHolder(binding.root){
        var dateText: TextView = binding.itemCalendarDateTv
        val coverImage: ImageView = binding.itemCalendarDateCoverImg
    }
}