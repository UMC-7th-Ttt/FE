package com.example.fe.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class UnifiedCalendarAdapter(private val months: List<MonthData>) : RecyclerView.Adapter<UnifiedCalendarAdapter.CalendarViewHolder>() {

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val monthTitle: TextView = itemView.findViewById(R.id.month_title_tv)
        val calendarRecyclerView: RecyclerView = itemView.findViewById(R.id.calendarRecyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val monthData = months[position]
        holder.monthTitle.text = "${monthData.year} ${monthData.month}"

        holder.calendarRecyclerView.layoutManager = GridLayoutManager(holder.itemView.context, 7)
        holder.calendarRecyclerView.adapter = DayAdapter(monthData.days)
    }

    override fun getItemCount() = months.size

    class DayAdapter(private val days: List<CalendarItem>) : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

        class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val dayTextView: TextView = itemView.findViewById(R.id.calendar_day_tv)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar_day, parent, false)
            return DayViewHolder(view)
        }

        override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
            val day = days[position]
            holder.dayTextView.text = day.date
            holder.dayTextView.background = day.imageResId?.let {
                ContextCompat.getDrawable(holder.itemView.context, it)
            }
        }


        override fun getItemCount() = days.size
    }
}