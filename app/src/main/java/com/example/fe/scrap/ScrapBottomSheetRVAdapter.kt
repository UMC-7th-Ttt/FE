package com.example.fe.scrap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.fe.R
import com.example.fe.databinding.FragmentScrapCustomToastBinding
import com.example.fe.databinding.ItemScrapBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ScrapBottomSheetRVAdapter(
    private val scrapList: List<Pair<String, Int>>,
    private val onItemSelected: (Boolean) -> Unit,
    private val parentFragment: BottomSheetDialogFragment // ScrapBottomSheetFragment 전달
) : RecyclerView.Adapter<ScrapBottomSheetRVAdapter.ScrapViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()

    inner class ScrapViewHolder(val binding: ItemScrapBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
        val binding = ItemScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScrapViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
        val (title, imageResId) = scrapList[position]
        val binding = holder.binding

        binding.scrapItemBottomSheetTitleTv.text = title
        binding.scrapItemBottomSheetIv.setImageResource(imageResId)

        // 선택된 아이템 상태 반영
        val isSelected = selectedItems.contains(position)
        binding.scrapBottomSheetPlusIv.setImageResource(
            if (isSelected) R.drawable.ic_scrap_plus_selected else R.drawable.ic_scrap_plus
        )

        // 아이템 클릭 이벤트
        val onItemClicked = {
            if (isSelected) {
                selectedItems.remove(position)
                onItemSelected(false) // 선택 해제 전달
            } else {
                selectedItems.add(position)
                onItemSelected(true)
                showCustomToast(binding.root, title, imageResId)
            }
            notifyItemChanged(position)
        }

        binding.scrapItemBottomSheetIv.setOnClickListener { onItemClicked() }
        binding.scrapItemBottomSheetTitleTv.setOnClickListener { onItemClicked() }
    }

    override fun getItemCount(): Int = scrapList.size

    private fun showCustomToast(view: View, title: String, imageResId: Int) {
        val inflater = LayoutInflater.from(view.context)
        val toastBinding = FragmentScrapCustomToastBinding.inflate(inflater)

        toastBinding.scrapItemIv.setImageResource(imageResId)
        toastBinding.scrapItemNameTv.text = "${title}에 저장됨"

        val toast = Toast(view.context).apply {
            duration = Toast.LENGTH_SHORT
            setView(toastBinding.root)
            setGravity(android.view.Gravity.TOP, 0, 100) // 위치 조정: 상단에 표시
        }

        toast.show()

        // ScrapBottomSheetFragment 닫기
        parentFragment.dismiss()
    }
}


//package com.example.fe.scrap
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import com.example.fe.R
//import com.example.fe.databinding.FragmentScrapCustomToastBinding
//import com.example.fe.databinding.ItemScrapBinding
//
//class ScrapBottomSheetRVAdapter(
//    private val scrapList: List<Pair<String, Int>>,
//    private val onItemSelected: (Boolean) -> Unit // 콜백 추가
//) : RecyclerView.Adapter<ScrapBottomSheetRVAdapter.ScrapViewHolder>() {
//
//    private val selectedItems = mutableSetOf<Int>()
//
//    inner class ScrapViewHolder(val binding: ItemScrapBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScrapViewHolder {
//        val binding = ItemScrapBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ScrapViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ScrapViewHolder, position: Int) {
//        val (title, imageResId) = scrapList[position]
//        val binding = holder.binding
//
//        binding.scrapItemBottomSheetTitleTv.text = title
//        binding.scrapItemBottomSheetIv.setImageResource(imageResId)
//
//        // 선택된 아이템 상태 반영
//        val isSelected = selectedItems.contains(position)
//        binding.scrapBottomSheetPlusIv.setImageResource(
//            if (isSelected) R.drawable.ic_scrap_plus_selected else R.drawable.ic_scrap_plus
//        )
//
//        // 아이템 클릭 이벤트
//        val onItemClicked = {
//            if (isSelected) {
//                selectedItems.remove(position)
//                onItemSelected(false) // 선택 해제 전달
//            } else {
//                selectedItems.add(position)
//                onItemSelected(true)
//                showCustomToast(binding.root, title, imageResId)
//            }
//            notifyItemChanged(position)
//        }
//
//        binding.scrapItemBottomSheetIv.setOnClickListener { onItemClicked() }
//        binding.scrapItemBottomSheetTitleTv.setOnClickListener { onItemClicked() }
//    }
//
//    override fun getItemCount(): Int = scrapList.size
//
//    private fun showCustomToast(view: View, title: String, imageResId: Int) {
//        val inflater = LayoutInflater.from(view.context)
//        val toastBinding = FragmentScrapCustomToastBinding.inflate(inflater)
//
//        toastBinding.scrapItemIv.setImageResource(imageResId)
//        toastBinding.scrapItemNameTv.text = "${title}에 저장됨"
//
//        val toast = Toast(view.context).apply {
//            duration = Toast.LENGTH_SHORT
//            setView(toastBinding.root)
//            setGravity(android.view.Gravity.TOP, 0, 100) // 위치 조정: 상단에 표시
//        }
//
//        toast.show()
//    }
//}
