import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fe.BookDetail.BookDetailActivity
import com.example.fe.BookLetter.BookDetail
import com.example.fe.databinding.ItemLetterBinding

class BookAdapter(private val bookList: List<BookDetail>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(val binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = bookList[position]
        with(holder.binding) {
            bookTitleTv.text = book.title // 책 제목
            bookAuthorTv.text = book.author // 작가 이름
            publisherTv.text = book.publisher // 출판사 이름

            // Glide로 표지 이미지 로드
            Glide.with(root.context)
                .load(book.cover)
                .into(bookIv)

            // 클릭하면 상세 페이지 이동
            bookInfoNextBtn.setOnClickListener {
                val context = root.context
                val intent = Intent(context, BookDetailActivity::class.java)
                intent.putExtra("BOOK_ID", book.bookId) // 책 ID 전달
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = bookList.size
}
