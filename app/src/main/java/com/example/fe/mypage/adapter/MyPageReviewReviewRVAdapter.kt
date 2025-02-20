package com.example.fe.mypage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.databinding.ItemMypageReviewReviewBinding
import com.example.fe.mypage.server.ReviewListResponse

class MyPageReviewReviewRVAdapter(private val itemClickListener: MyItemClickListener) :
    RecyclerView.Adapter<MyPageReviewReviewRVAdapter.ViewHolder>() {
    private val mypageReviews = ArrayList<ReviewListResponse.Result.Review>()

    interface MyItemClickListener {
        fun onItemClick(review: ReviewListResponse.Result.Review)
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

    inner class ViewHolder(val binding: ItemMypageReviewReviewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(review: ReviewListResponse.Result.Review) {
            Glide.with(binding.mypageReviewReviewIv.context)
                .load(review.cover)
                .into(binding.mypageReviewReviewIv)
            binding.root.setOnClickListener {
                itemClickListener.onItemClick(review)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setReview(reviews: List<ReviewListResponse.Result.Review>) {
        this.mypageReviews.clear()  // 기존 데이터 제거
        this.mypageReviews.addAll(reviews)  // 새로운 데이터 추가
        notifyDataSetChanged()  // 데이터 변경 알림
    }
}