package com.example.fe.Home.Category

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R

class HomeCategoryAdapter(private val categoryList: List<HomeBook>) :
    RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTitle: TextView = view.findViewById(R.id.category_title)
        val bookRecyclerView: RecyclerView = view.findViewById(R.id.book_recycler_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        holder.categoryTitle.text = category.title

        // 가로 방향 리사이클러뷰 설정
        holder.bookRecyclerView.layoutManager =
            LinearLayoutManager(holder.itemView.context, RecyclerView.HORIZONTAL, false)
        holder.bookRecyclerView.adapter = HomeBookAdapter(category.books)
    }

    override fun getItemCount(): Int = categoryList.size
}