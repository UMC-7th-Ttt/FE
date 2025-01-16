package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.bookclub_book.dataclass.BookclubByMonth
import com.example.fe.databinding.ItemBookclubBookMonthsBinding

class BookclubByMonthRVAdapter():RecyclerView.Adapter<BookclubByMonthRVAdapter.ViewHolder>() {
    private val bookclubsByMonth = ArrayList<BookclubByMonth>()

//    interface MyItemClickListener{
//        fun onRemoveSong(songId: Int)
//    }
//
//    private lateinit var mItemClickListener : MyItemClickListener
//
//    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
//        mItemClickListener = itemClickListener
//    }

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
            binding.itemBookclubBookMonthTv.text = bookclubByMonth.month
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookclubByMonth(bookclubsByMonth: List<BookclubByMonth>) {
        this.bookclubsByMonth.clear()  // 기존 데이터 제거
        this.bookclubsByMonth.addAll(bookclubsByMonth)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}