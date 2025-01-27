package com.example.fe

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.databinding.ActivityBookclubPlaceBinding
class BookclubPlaceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBookclubPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBookclubPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // FrameLayout에 PlaceListFragment 추가
        supportFragmentManager.beginTransaction()
            .replace(R.id.bookclub_place_frm, BookclubPlaceListFragment())
            .commit()

        // 여러 버튼이 BookclubPlaceSearchFragment로 이동하도록 설정
        val commonClickListener = View.OnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.bookclub_place_frm, BookclubPlaceSearchFragment())
                .addToBackStack(null) // 뒤로가기 가능
                .commit()
        }

        binding.bookclubPlaceTitleTv.setOnClickListener(commonClickListener)
        binding.bookclubPlaceArrowDownIc.setOnClickListener(commonClickListener)
    }

    fun hideTopViews() {
        binding.bookclubPlaceTitleTv.visibility = View.GONE
        binding.bookclubPlaceArrowDownIc.visibility = View.GONE
        binding.bookclubPlaceSearchIc.visibility = View.GONE
        binding.bookclubPlacePersonIc.visibility = View.GONE
    }

    fun showTopViews() {
        binding.bookclubPlaceTitleTv.visibility = View.VISIBLE
        binding.bookclubPlaceArrowDownIc.visibility = View.VISIBLE
        binding.bookclubPlaceSearchIc.visibility = View.VISIBLE
        binding.bookclubPlacePersonIc.visibility = View.VISIBLE
    }
}
