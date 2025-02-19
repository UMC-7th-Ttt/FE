import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.fe.Home.Category.HomeBook
import com.example.fe.Home.Category.HomeCategory
import com.example.fe.Home.Category.HomeCategoryAdapter
import com.example.fe.Home.HomeApiService
import com.example.fe.Home.HomeResponse
import com.example.fe.Home.HomeResult
import com.example.fe.Home.ViewPagerAdapter
import com.example.fe.Notification.NotificationActivity
import com.example.fe.databinding.FragmentHomeBinding
import com.example.fe.network.RetrofitObj
import com.example.fe.search.SearchMainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeService = RetrofitObj.getRetrofit().create(HomeApiService::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("HomeFragment", "🟢 HomeFragment 실행됨")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupHomeData()

        binding.notificationIcon.setOnClickListener {
            val intent = Intent(requireContext(), com.example.fe.BookLetter.LetterActivity::class.java) //이부분 NotificationActivity로 반드시바꿔나야함!!
            startActivity(intent)
        }
        binding.searchIcon.setOnClickListener {
            val intent = Intent(requireContext(), SearchMainActivity::class.java)//이부분 나중에 검색쪽으로 변경필요
            startActivity(intent)
        }
    }

    private fun setupHomeData() {
        val token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTc0MDMxMTY3MywiZW1haWwiOiJhZG1pbjJAbmF2ZXIuY29tIn0.JwzCFHzkGRW-CESnhvcFUG6gc55MH1q10uEHvp12qubguOuKZXsQZyVrAY2mADTmwWDecC9tC5reXLh6tUR-kg"
        homeService.getHomeData("Bearer $token").enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    val homeData = response.body()?.result
                    if (homeData != null) {
                        Log.d("API", "✅ 데이터 불러오기 성공")
                        updateUI(homeData)
                    }
                } else {
                    Log.e("API", "❌ 응답 실패: ${response.errorBody()?.string()}")
                    Toast.makeText(requireContext(), "데이터를 불러오지 못했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("API", "❌ API 호출 실패", t)
                Toast.makeText(requireContext(), "네트워크 오류 발생", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(data: HomeResult) {
        // Fragment가 Activity에 연결되어 있는지 확인
        if (!isAdded) return

        binding.greetingText.text = "안녕하세요, ${data.nickname}님!\n오늘은 어떤 책을 시작해볼까요?"

        // Safe call을 사용하여 Glide 호출
        context?.let {
            Glide.with(it)
                .load(data.profileUrl)
                .into(binding.profileIcon)
        }

        binding.viewPager.adapter = ViewPagerAdapter(data.mainBannerList)

        if (data.bookClubList.isNotEmpty()) {
            val bookClub = data.bookClubList[0] // 첫 번째 북클럽 정보만 표시
            binding.activityCard.bookTitle.text = bookClub.bookTitle
            binding.activityCard.progressBar.progress = bookClub.completionRate
            binding.activityCard.progressPercentage.text = "${bookClub.completionRate}% 완료"

            // Safe call을 사용하여 Glide 호출
            context?.let {
                Glide.with(it)
                    .load(bookClub.bookCover)
                    .into(binding.activityCard.bookImage)
            }
        }

        // ✅ remindReviewList 데이터 적용
        if (data.remindReviewList.isNotEmpty()) {
            val review1 = data.remindReviewList.getOrNull(0) // 첫 번째 리뷰 데이터
            val review2 = data.remindReviewList.getOrNull(1) // 두 번째 리뷰 데이터 (없을 수도 있음)

            if (review1 != null) {
                binding.finalCard.card1Title.text = review1.bookTitle
                binding.finalCard.card1Description.text = review1.content
                binding.finalCard.card1Date.text = review1.writeDate

                // Safe call을 사용하여 Glide 호출
                context?.let {
                    Glide.with(it)
                        .load(review1.bookCover)
                        .into(binding.finalCard.card1Image)
                }
            }

            if (review2 != null) {
                binding.finalCard.card2Title.text = review2.bookTitle
                binding.finalCard.card2Description.text = review2.content
                binding.finalCard.card2Date.text = review2.writeDate

                // Safe call을 사용하여 Glide 호출
                context?.let {
                    Glide.with(it)
                        .load(review2.bookCover)
                        .into(binding.finalCard.card2Image)
                }
            }
        }

        val categoryList = data.bookLetterList.map {
            HomeCategory(it.bookLetterTitle, it.bookList.map { book -> HomeBook(book.bookCoverImg) })
        }

        binding.verticalRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.verticalRecyclerView.adapter = HomeCategoryAdapter(categoryList)
    }


}