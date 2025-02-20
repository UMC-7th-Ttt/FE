package com.example.fe.bookclub_book

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.JohnRetrofitClient
import com.example.fe.bookclub_book.adapter.BookclubByMonthRVAdapter
import com.example.fe.bookclub_book.adapter.BookclubMemberRVAdapter
import com.example.fe.bookclub_book.server.BookClubByMonthResponse
import com.example.fe.bookclub_book.server.ReadingRecordsListResponse
import com.example.fe.bookclub_book.server.BookClubRetrofitInterface
import com.example.fe.databinding.FragmentBookclubBookHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookclubBookHomeFragment : Fragment() {

    private lateinit var binding: FragmentBookclubBookHomeBinding
    private lateinit var bookclubMemberRVAdapter: BookclubMemberRVAdapter
    private lateinit var bookclubByMonthRVAdapter: BookclubByMonthRVAdapter

    companion object {
        private const val REQUEST_READ_MEDIA_IMAGES = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookHomeBinding.inflate(inflater, container, false)

        if (ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_MEDIA_IMAGES),
                REQUEST_READ_MEDIA_IMAGES)
        } else {
            initViews()
        }

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_MEDIA_IMAGES) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initViews()
            } else {
                Log.e("BookclubBookHomeFragment", "Permission denied to read media images")
            }
        }
    }

    private fun initViews() {
        initBookclubMemberRecyclerview()
        initBookclubByMonthRecyclerview()
        fetchReadingRecords()
        fetchBookClubMonth()
    }

    private fun initBookclubMemberRecyclerview() {
        binding.bookclubBookHomeMembersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        bookclubMemberRVAdapter = BookclubMemberRVAdapter(object : BookclubMemberRVAdapter.MyItemClickListener {
            override fun onItemClick(readingRecordId: Int) {
                val intent = Intent(context, BookclubBookReviewDetail::class.java)
                intent.putExtra("readingRecordId", readingRecordId)
                startActivity(intent)
            }
        })

        binding.bookclubBookHomeMembersRv.adapter = bookclubMemberRVAdapter
    }

    private fun initBookclubByMonthRecyclerview() {
        binding.bookclubBookHomeMonthRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        bookclubByMonthRVAdapter = BookclubByMonthRVAdapter(object : BookclubByMonthRVAdapter.MyItemClickListener {
            override fun onItemClick(bookClub: BookClubByMonthResponse.Result.BookClub) {
                val intent = Intent(context, BookclubJoin::class.java)
                intent.putExtra("bookClubId", bookClub.bookClubId)
                startActivity(intent)
            }
        })

        binding.bookclubBookHomeMonthRv.adapter = bookclubByMonthRVAdapter
    }

    private fun fetchReadingRecords() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(BookClubRetrofitInterface::class.java)
        api.getReadingRecords().enqueue(object : Callback<ReadingRecordsListResponse>  {
            override fun onResponse(call: Call<ReadingRecordsListResponse>, response: Response<ReadingRecordsListResponse>) {
                if (response.isSuccessful) {
                    val readingRecordsResponse = response.body()
                    readingRecordsResponse?.let {
                        bookclubMemberRVAdapter.setMembers(it.result.readingRecords)
                        Log.d("BookclubBookHomeFragment", "Reading records fetched: ${it.result.readingRecords.size}")
                    }
                } else {
                    Log.e("BookclubBookHomeFragment", "Failed to fetch reading records: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ReadingRecordsListResponse>, t: Throwable) {
                Log.e("BookclubBookHomeFragment", "Network Error: ${t.message}")
            }
        })
    }

    private fun fetchBookClubMonth() {
        val api = JohnRetrofitClient.getClient(requireContext()).create(BookClubRetrofitInterface::class.java)
        api.getBookClubByMonth().enqueue(object : Callback<BookClubByMonthResponse> {
            override fun onResponse(call: Call<BookClubByMonthResponse>, response: Response<BookClubByMonthResponse>) {
                if (response.isSuccessful) {
                    val bookClubMonthResponse = response.body()
                    bookClubMonthResponse?.result?.let { result ->
                        bookclubByMonthRVAdapter.setBookclubByMonth(result.bookClubs)
                        binding.bookclubBookHomeMonthTv.text = "${result.currentMonth}월 북클럽"
                        Log.d("BookclubBookHomeFragment", "Book clubs by month fetched: ${result.bookClubs.size}")
                    }
                } else {
                    Log.e("BookclubBookHomeFragment", "Failed to fetch book clubs by month: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<BookClubByMonthResponse>, t: Throwable) {
                Log.e("BookclubBookHomeFragment", "Network Error: ${t.message}")
            }
        })
    }
}