package com.example.fe.search

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.databinding.ItemSearchResultBookBinding
import com.example.fe.search.api.BookResponse
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.scrap.ScrapBottomSheetFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchResultBookRVAdapter(private val bookList: List<BookResponse>) :
    RecyclerView.Adapter<SearchResultBookRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemSearchResultBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: BookResponse) {
            binding.apply {
                Glide.with(root.context)
                    .load(book.cover)
                    .placeholder(R.drawable.img_book_cover1)
                    .into(itemSearchResultBookCoverIv)

                itemSearchResultBookTitleTv.text = book.title
                itemSearchResultBookAuthorTv.text = book.author
                itemSearchResultBookCategoryTv.text = book.category
                itemSearchResultBookPublisherTv.text = "출판사 ${book.publisher}"

                updateBookmarkUI(book.isScraped)

                // 북마크 버튼 클릭 이벤트 추가
                itemBookmarkIv.setOnClickListener {
                    if (book.isScraped) {
                        deleteScrap(book)
                    } else {
                        showScrapBottomSheet(book)
                    }
                }
            }
        }

        // 북마크 상태 UI 업데이트
        private fun updateBookmarkUI(isScraped: Boolean) {
            binding.itemBookmarkIv.setImageResource(
                if (isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
            )
        }

        // 북마크(스크랩) 삭제 API 호출
        private fun deleteScrap(book: BookResponse) {
            RetrofitClient.scrapApi.deleteBookScrap(book.bookId)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            book.isScraped = false // 스크랩 해제
                            updateBookmarkUI(false)
                            showToast("스크랩이 취소되었습니다")
                        } else {
                            Log.e("ScrapAPI", "❌ 스크랩 취소 실패: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Log.e("ScrapAPI", "❌ 네트워크 오류: ${t.message}")
                    }
                })
        }

        // 스크랩 추가 바텀시트 띄우기
        private fun showScrapBottomSheet(book: BookResponse) {
            val scrapBottomSheet = ScrapBottomSheetFragment(
                bookId = book.bookId,  // 도서 ID 전달
                placeId = null, // 장소 스크랩이 아니므로 null
                onBookmarkStateChanged = { isSelected ->
                    book.isScraped = isSelected
                    updateBookmarkUI(isSelected)
                }
            )
            scrapBottomSheet.show(
                (binding.root.context as AppCompatActivity).supportFragmentManager,
                scrapBottomSheet.tag
            )
        }

        // 토스트 메시지 표시
        private fun showToast(message: String) {
            Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemSearchResultBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}



//package com.example.fe.search
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.example.fe.R
//import com.example.fe.databinding.ItemSearchResultBookBinding
//import com.example.fe.search.api.BookResponse
//
//class SearchResultBookRVAdapter(private val bookList: List<BookResponse>) :
//    RecyclerView.Adapter<SearchResultBookRVAdapter.BookViewHolder>() {
//
//    inner class BookViewHolder(val binding: ItemSearchResultBookBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
//        val binding = ItemSearchResultBookBinding.inflate(
//            LayoutInflater.from(parent.context),
//            parent,
//            false
//        )
//        return BookViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
//        val book = bookList[position]
//        holder.binding.apply {
//            Glide.with(root.context)
//                .load(book.cover)
//                .placeholder(R.drawable.img_book_cover1)
//                .into(itemSearchResultBookCoverIv)
//
//            itemSearchResultBookTitleTv.text = book.title
//            itemSearchResultBookAuthorTv.text = book.author
//            itemSearchResultBookCategoryTv.text = book.category
//            itemSearchResultBookPublisherTv.text = "출판사 ${book.publisher}"
//
//            itemBookmarkIv.setImageResource(
//                if (book.isScraped) R.drawable.ic_bookmark_selected else R.drawable.ic_bookmark
//            )
//        }
//    }
//
//    override fun getItemCount(): Int = bookList.size
//}
