package com.example.fe.scrap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentScrapCustomToastBinding
import com.example.fe.databinding.ItemScrapBinding
import com.example.fe.mypage.ScrapFolderResponse
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapBottomSheetRVAdapter(
    private val scrapFolders: List<ScrapFolderResponse.Result.Folder>,
    private val bookId: Int?, // 도서 스크랩 ID (도서일 경우)
    private val placeId: Int?, // 장소 스크랩 ID (공간일 경우)
    private val onItemSelected: (Boolean) -> Unit,
    private val parentFragment: BottomSheetDialogFragment
) : RecyclerView.Adapter<ScrapBottomSheetRVAdapter.ScrapViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    inner class ScrapViewHolder(val binding: ItemScrapBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val binding = ItemScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        val folder = scrapFolders[position]
        val binding = holder.binding

        binding.scrapItemBottomSheetTitleTv.text = folder.name

        // 이미지 설정 (images 리스트에서 첫 번째 이미지 사용)
        if (folder.images.isNotEmpty()) {
            Glide.with(binding.root.context)
                .load(folder.images[0])
                .placeholder(R.drawable.img_scrap_book)
                .into(binding.scrapItemBottomSheetIv)
        } else {
            binding.scrapItemBottomSheetIv.setImageResource(R.drawable.img_scrap_book)
        }

        // 선택된 아이템 상태 반영
        val isSelected = selectedItems.contains(position)
        binding.scrapBottomSheetPlusIv.setImageResource(
            if (isSelected) R.drawable.ic_scrap_plus_selected else R.drawable.ic_scrap_plus
        )

        // 아이템 클릭 이벤트 (스크랩 API 호출 추가)
        val onItemClicked = {
            if (isSelected) {
                selectedItems.remove(position)
                onItemSelected(false) // 선택 해제 전달
            } else {
                selectedItems.add(position)
                onItemSelected(true)
                scrapItem(binding.root, folder.name, folder.images.firstOrNull()) // API 호출
            }
            notifyItemChanged(position)
        }

        binding.scrapItemBottomSheetIv.setOnClickListener { onItemClicked() }
        binding.scrapItemBottomSheetTitleTv.setOnClickListener { onItemClicked() }
    }

    override fun getItemCount(): Int = scrapFolders.size

    // 스크랩 API 호출 후 토스트 메시지 표시
    private fun scrapItem(view: View, folderName: String, imageUrl: String?) {
        if (bookId != null) {
            // 도서 스크랩 API 호출
            RetrofitClient.scrapApi.scrapBook(bookId, folderName).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showCustomToast(view, folderName, imageUrl)
                    } else {
                        Toast.makeText(view.context, "스크랩 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(view.context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (placeId != null) {
            // 공간 스크랩 API 호출
            RetrofitClient.scrapApi.scrapPlace(placeId, folderName).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        showCustomToast(view, folderName, imageUrl)
                    } else {
                        Toast.makeText(view.context, "스크랩 실패", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(view.context, "네트워크 오류", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showCustomToast(view: View, title: String, imageUrl: String?) {
        val inflater = LayoutInflater.from(view.context)
        val toastBinding = FragmentScrapCustomToastBinding.inflate(inflater)

        if (!imageUrl.isNullOrEmpty()) {
            Glide.with(view.context)
                .load(imageUrl)
                .placeholder(R.drawable.img_scrap_book)
                .into(toastBinding.scrapItemIv)
        } else {
            toastBinding.scrapItemIv.setImageResource(R.drawable.img_scrap_book)
        }

        toastBinding.scrapItemNameTv.text = "${title}에 저장됨"

        val toast = Toast(view.context).apply {
            duration = Toast.LENGTH_SHORT
            setView(toastBinding.root)
            setGravity(android.view.Gravity.TOP, 0, 100)
        }

        toast.show()

        // ScrapBottomSheetFragment 닫기
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            parentFragment.dismiss()
        }, 300)

    }
}