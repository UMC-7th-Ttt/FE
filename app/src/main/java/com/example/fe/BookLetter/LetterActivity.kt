import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.fe.BookLetter.BookLetterDetail
import com.example.fe.BookLetter.BookLetterResponse
import com.example.fe.BookLetter.BookLetterService
import com.example.fe.BookLetter.LetterAdapter
import com.example.fe.BookLetter.LetterBannerItem
import com.example.fe.R
import com.example.fe.databinding.ActivityLetterBinding
import com.example.fe.network.RetrofitObj
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  LetterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterBinding
    private lateinit var bookLetterService: BookLetterService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding 초기화
        binding = ActivityLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrofit 서비스 초기화
        bookLetterService = RetrofitObj.getRetrofit().create(BookLetterService::class.java)

        // 뒤로 가기 버튼 클릭 이벤트
        binding.backButton.setOnClickListener {
            finish()
        }

        // 북레터 ID 가져오기 (예: 인텐트로 전달받은 값)
        val bookLetterId = intent.getLongExtra("BOOK_LETTER_ID", 0L)

        // 북레터 ID 예외 처리
        if (bookLetterId == 0L) {
            binding.letterIntro.text = "잘못된 북레터 ID입니다."
            return
        }

        // API 호출
        fetchBookLetterDetail(bookLetterId)
    }

    private fun fetchBookLetterDetail(bookLetterId: Long) {
        val token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBY2Nlc3NUb2tlbiIsImV4cCI6MTczOTc2ODE2MiwiZW1haWwiOiJtb2Rlc3RuYXR1cmVAbmF2ZXIuY29tIn0.rGfiXRBkJ1x1mpFl5I1LDBClf_TddLfj0e0l_YfgtWU-DqEQ83yXNdicdBmz9k8tmAyqK5iNHAafTdvKo8RIsg" // 실제 토큰으로 변경
        val call = bookLetterService.getBookLetterDetail(token, bookLetterId)

        call.enqueue(object : Callback<BookLetterResponse> {
            override fun onResponse(call: Call<BookLetterResponse>, response: Response<BookLetterResponse>) {
                if (response.isSuccessful) {
                    val bookLetterDetail = response.body()?.result
                    if (bookLetterDetail != null) {
                        // 데이터 바인딩
                        bindDataToUI(bookLetterDetail)
                    }
                } else {
                    // API 요청 실패 처리
                    binding.letterIntro.text = "북레터 데이터를 불러올 수 없습니다."
                    binding.mainBanner.visibility = View.GONE // 배너 숨김
                    binding.bookRecycler.visibility = View.GONE // 책 목록 숨김
                }
            }

            override fun onFailure(call: Call<BookLetterResponse>, t: Throwable) {
                // 네트워크 에러 처리
                binding.letterIntro.text = "네트워크 오류 발생"
            }
        })
    }

    private fun bindDataToUI(bookLetterDetail: BookLetterDetail) {
        // 📌 배너(ViewPager2) 설정
        val bannerList = listOf(
            LetterBannerItem(
                imageRes = bookLetterDetail.coverImg, // cover_img -> 배너 이미지
                title = bookLetterDetail.title,       // title -> 배너 제목
                subtitle = bookLetterDetail.subtitle, // subtitle -> 배너 부제목
                author = bookLetterDetail.editor      // editor -> 배너 작가
            )
        )
        binding.mainBanner.adapter = LetterAdapter(bannerList)
        binding.mainBanner.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        // 📌 북레터 콘텐츠 소개 바인딩 (content -> letter_intro)
        binding.letterIntro.text = bookLetterDetail.content

        // 📌 제공 도서 목록 바인딩 (books -> book_recycler)
        binding.bookRecycler.layoutManager = LinearLayoutManager(this)
        binding.bookRecycler.adapter = BookAdapter(bookLetterDetail.books)
    }
}
