package com.example.fe.bookclub_book.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fe.bookclub_book.dataclass.BookClubByMonthResponse
import com.example.fe.databinding.ItemBookclubBookMonthsBinding

class BookclubByMonthRVAdapter(private val itemClickListener: MyItemClickListener) : RecyclerView.Adapter<BookclubByMonthRVAdapter.ViewHolder>() {
    private val bookclubsByMonth = ArrayList<BookClubByMonthResponse.Result.BookClub>()

    interface MyItemClickListener {
        fun onItemClick(bookClub: BookClubByMonthResponse.Result.BookClub)
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

    inner class ViewHolder(val binding: ItemBookclubBookMonthsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(bookclubByMonth: BookClubByMonthResponse.Result.BookClub) {
            binding.titleTv.text = bookclubByMonth.bookTitle
            Glide.with(binding.bookclubMonthIv.context)
                .load(bookclubByMonth.bookCover)
                .apply(RequestOptions().override(450, 600)) // 해상도 설정
                .into(binding.bookclubMonthIv)

            binding.root.setOnClickListener {
                itemClickListener.onItemClick(bookclubByMonth)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setBookclubByMonth(bookclubsByMonth: List<BookClubByMonthResponse.Result.BookClub>) {
        this.bookclubsByMonth.clear()  // 기존 데이터 제거
        this.bookclubsByMonth.addAll(bookclubsByMonth)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}