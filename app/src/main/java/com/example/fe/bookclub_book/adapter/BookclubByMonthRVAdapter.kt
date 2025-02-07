package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.bookclub_book.dataclass.BookclubByMonth
import com.example.fe.databinding.ItemBookclubBookMonthsBinding

class BookclubByMonthRVAdapter(private val itemClickListener: MyItemClickListener) :RecyclerView.Adapter<BookclubByMonthRVAdapter.ViewHolder>() {
    private val bookclubsByMonth = ArrayList<BookclubByMonth>()

    interface MyItemClickListener {
        fun onItemClick(participation: ArrayList<BookclubByMonth>)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBookclubBookMonthsBinding = ItemBookclubBookMonthsBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bookclubsByMonth[position])
    }

    override fun getItemCount(): Int = bookclubsByMonth.size


    inner class ViewHolder(val binding: ItemBookclubBookMonthsBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(bookclubByMonth: BookclubByMonth) {
//            binding.itemBookclubBookMonthIv.setImageResource(bookclubByMonth.coverImg!!)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(bookclubsByMonth)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookclubByMonth(bookclubsByMonth: List<BookclubByMonth>) {
        this.bookclubsByMonth.clear()  // 기존 데이터 제거
        this.bookclubsByMonth.addAll(bookclubsByMonth)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}