package com.example.fe.search

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.R
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.databinding.FragmentScrapCancelCustomToastBinding
import com.example.fe.databinding.ItemRecommendedBookBinding
import com.example.fe.scrap.ScrapBottomSheetFragment
import com.example.fe.search.api.BookResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendedBookListRVAdapter(
    private val context: Context, // Context 전달
    private val bookList: List<BookResponse>
) :
    RecyclerView.Adapter<RecommendedBookListRVAdapter.BookViewHolder>() {

    inner class BookViewHolder(private val binding: ItemRecommendedBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookResponse) {
            // 커버 이미지 설정 (Glide 사용)
            Glide.with(binding.root.context)
                .load(book.cover)
                .placeholder(R.drawable.img_book_cover7) // 기본 이미지 설정
                .into(binding.itemRecommendedBookIv)

            // 도서 제목 및 저자 설정
            binding.itemRecommendedBookTitleTv.text = book.title
            binding.itemRecommendedBookAuthorTv.text = book.author

            // 북마크 아이콘 설정
            updateBookmarkUI(book.isScraped)

            // 북마크 버튼 클릭 이벤트 추가
            binding.itemBookmarkIv.setOnClickListener {
                if (book.isScraped) {
                    deleteScrap(book)
                } else {
                    showScrapBottomSheet(book)
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
            RetrofitClient.scrapApi.deleteBookScrap(book.id)
                .enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            book.isScraped = false // 스크랩 해제
                            updateBookmarkUI(false)

                            // LayoutInflater 수정
                            val inflater = LayoutInflater.from(binding.root.context)
                            val toastBinding = FragmentScrapCancelCustomToastBinding.inflate(inflater)

                            // 토스트 메시지 설정
                            toastBinding.scrapCancelTv.text = "스크랩 취소되었습니다!"

                            // 커스텀 토스트 생성 및 표시
                            val toast = Toast(binding.root.context).apply {
                                duration = Toast.LENGTH_SHORT
                                view = toastBinding.root
                                setGravity(android.view.Gravity.TOP, 0, 100)
                            }
                            toast.show()
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
                bookId = book.id,  // 도서 ID 전달
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemRecommendedBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position])
    }

    override fun getItemCount(): Int = bookList.size
}