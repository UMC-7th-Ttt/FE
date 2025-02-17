package com.example.fe.Home.Category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.databinding.ItemCategoryBinding

class HomeCategoryAdapter(private val categoryList: List<HomeCategory>) :
    RecyclerView.Adapter<HomeCategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: HomeCategory) {
            binding.categoryTitle.text = category.title
            binding.bookRecyclerView.layoutManager = LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.bookRecyclerView.adapter = HomeBookAdapter(category.books)
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
