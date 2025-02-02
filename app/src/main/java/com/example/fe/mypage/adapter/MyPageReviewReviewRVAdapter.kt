package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemMypageReviewReviewBinding
import com.example.fe.mypage.MyPageReview

class MyPageReviewReviewRVAdapter(private val itemClickListener: MyItemClickListener) :
    RecyclerView.Adapter<MyPageReviewReviewRVAdapter.ViewHolder>() {
    private val mypageReviews = ArrayList<MyPageReview>()

    interface MyItemClickListener {
        fun onItemClick(myPageReview: MyPageReview)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMypageReviewReviewBinding = ItemMypageReviewReviewBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mypageReviews[position])
    }

    override fun getItemCount(): Int = mypageReviews.size


    inner class ViewHolder(val binding: ItemMypageReviewReviewBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myPageReview: MyPageReview) {
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(myPageReview)
            }

        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setReview(myPageReview: List<MyPageReview>) {
        this.mypageReviews.clear()  // 기존 데이터 제거
        this.mypageReviews.addAll(myPageReview)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}