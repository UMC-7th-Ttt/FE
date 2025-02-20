package com.example.fe.bookclub_book.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemCertifyPhotoBinding

class CertifyPhotoRVAdapter(
    private val images: MutableList<Uri>,
    private val listener: OnPhotoClickListener
) : RecyclerView.Adapter<CertifyPhotoRVAdapter.ViewHolder>() {

    interface OnPhotoClickListener {
        fun onPhotoDeleteClick(position: Int)
    }

    inner class ViewHolder(private val binding: ItemCertifyPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            Glide.with(binding.root.context)
                .load(uri)
                .into(binding.photoItemIv)

            binding.deletePhotoBtn.setOnClickListener {
                listener.onPhotoDeleteClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCertifyPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size
}