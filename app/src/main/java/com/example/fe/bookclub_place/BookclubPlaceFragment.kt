package com.example.fe.bookclub_place

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fe.bookclub_place.api.PlaceSearchResponse
import com.example.fe.R
import com.example.fe.bookclub_place.api.RetrofitClient
import com.example.fe.search.SearchMainActivity
import com.example.fe.databinding.FragmentBookclubPlaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubPlaceFragment : Fragment() {

    private lateinit var binding: FragmentBookclubPlaceBinding

    // 검색 결과를 받을 ActivityResultLauncher 등록
    private val searchActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val keyword = result.data?.getStringExtra("KEYWORD") ?: "검색 결과"
            Log.d("BookclubPlaceFragment", "✅ 받은 키워드: $keyword")
            updateKeywordToTitle(keyword) // UI 업데이트
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookclubPlaceBinding.inflate(inflater, container, false)

//         기본 프래그먼트 설정 (BookclubPlaceListFragment 초기 화면)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.bookclub_place_list_frm, BookclubPlaceListFragment())
            .commit()

        initBookclubPlaceSearchClickListener()
        initSearchClickListener()

        fetchPlacesFromApi()

        return binding.root
    }

    private fun fetchPlacesFromApi() {
        val keyword = binding.bookclubPlaceTitleTv.text.toString().trim() // 🔥 현재 키워드 가져오기
        if (keyword.isEmpty()) {
            Log.e("BookclubPlaceFragment", "❌ 검색 키워드가 비어있음")
            return
        }

        Log.d("BookclubPlaceFragment", "✅ API 요청 실행: $keyword")
    }

    // 공통 클릭 리스너 설정 (BookclubPlaceSearchFragment로 이동)
    private fun initBookclubPlaceSearchClickListener() {
        val commonClickListener = View.OnClickListener {
            val intent = Intent(requireContext(), BookclubPlaceSearchActivity::class.java)
//            startActivity(intent)

            searchActivityLauncher.launch(intent) // 결과를 받을 수 있도록 변경
        }

        binding.bookclubPlaceTitleTv.setOnClickListener(commonClickListener)
        binding.bookclubPlaceArrowDownIc.setOnClickListener(commonClickListener)

    }

    // 메인 검색 activity로 이동
    private fun initSearchClickListener() {
        binding.bookclubPlaceSearchIc.setOnClickListener {
            val intent = Intent(requireContext(), SearchMainActivity::class.java)
            startActivity(intent)
        }
    }

    // 받은 키워드를 UI에 반영
    private fun updateKeywordToTitle(keyword: String) {
        Log.d("BookclubPlaceFragment", "✅ 키워드 업데이트됨: $keyword")
        binding.bookclubPlaceTitleTv.text = keyword

        fetchPlacesFromApi() // 새로운 키워드로 검색 실행

        updateBookclubPlaceListFragment(keyword)
    }

    // `BookclubPlaceListFragment`를 업데이트하는 함수
    private fun updateBookclubPlaceListFragment(keyword: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.bookclub_place_list_frm, BookclubPlaceListFragment().apply {
                arguments = Bundle().apply {
                    putString("KEYWORD", keyword)
                }
            })
            .addToBackStack(null)
            .commit()
    }

}
