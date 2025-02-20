package com.example.fe.Home.Category

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.BookLetter.LetterActivity
import com.example.fe.databinding.ItemCategoryBinding





import kotlin.math.abs


class HomeCategoryAdapter(private val categoryList: List<HomeCategory>) :
    RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    private var startX = 0f // 터치 시작 X 좌표
    private val touchThreshold = 20 // 최소 이동 거리 (가로 스크롤 감지 기준)
    private var startY = 0f // 터치 시작 Y 좌표

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: HomeCategory) {
            binding.categoryTitle.text = category.title
            binding.bookRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.bookRecyclerView.adapter = HomeBookAdapter(category.books)


            if (binding.bookRecyclerView.itemDecorationCount == 0) {
                binding.bookRecyclerView.addItemDecoration(SpaceItemDecoration(16)) // 16dp 간격 추가
            }



            // ✅ bookRecyclerView에서 스크롤할 때는 클릭 이벤트가 실행되지 않도록 함
            binding.bookRecyclerView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        startX = event.x
                        startY = event.y
                    }
                    MotionEvent.ACTION_UP -> {
                        val diffX = abs(event.x - startX)
                        val diffY = abs(event.y - startY)
                        if (diffX < touchThreshold && diffY < touchThreshold) {
                            binding.root.performClick() // 부모 클릭 실행
                        }
                    }
                }
                false
            }



            // ✅ 북레터 클릭 시 LetterActivity로 이동 (bookLetterId 전달)
            binding.root.setOnClickListener {
                val context = binding.root.context

                Log.d("HomeCategoryAdapter", "📡 클릭됨! bookLetterId: ${category.bookLetterId}")

                val intent = Intent(context, LetterActivity::class.java)
                intent.putExtra("bookLetterId", category.bookLetterId) // ✅ bookLetterId 전달
                context.startActivity(intent)

                Log.d("HomeCategoryAdapter", "📡 Intent 실행 완료 (bookLetterId: ${category.bookLetterId})")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size
}
