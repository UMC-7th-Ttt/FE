package com.example.fe.bookclub_book

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.BookclubBookReview
import com.example.fe.BookclubJoin
import com.example.fe.bookclub_book.dataclass.BookclubByMonth
import com.example.fe.bookclub_book.dataclass.BookclubMember
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookclubByMonthRVAdapter
import com.example.fe.bookclub_book.adapter.BookclubMemberRVAdapter
import com.example.fe.databinding.FragmentBookclubBookHomeBinding


class BookclubBookHomeFragment : Fragment() {

    private lateinit var binding: FragmentBookclubBookHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookHomeBinding.inflate(inflater, container, false)

        initBookclubMemberRecyclerview()
        initBookclubByMonthRecyclerview()

        return binding.root
    }

    private fun initBookclubMemberRecyclerview() {
        binding.bookclubBookHomeMembersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.bookclubBookHomeMonthRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bookclubMemberRVAdapter = BookclubMemberRVAdapter(object : BookclubMemberRVAdapter.MyItemClickListener {
            override fun onItemClick(participation: ArrayList<BookclubMember>) {
                val intent = Intent(context, BookclubBookReview::class.java)
                startActivity(intent)
            }
        })

        val dummyMembers = listOf(
            BookclubMember("주디", R.drawable.img_bookclub_member),
            BookclubMember("파도", R.drawable.img_bookclub_member),
            BookclubMember("민준", R.drawable.img_bookclub_member),
            BookclubMember("현규", R.drawable.img_bookclub_member)
        )


        bookclubMemberRVAdapter.setMembers(dummyMembers)

        binding.bookclubBookHomeMembersRv.adapter = bookclubMemberRVAdapter

    }

    private fun initBookclubByMonthRecyclerview() {
        binding.bookclubBookHomeMonthRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bookclubByMonthRVAdapter = BookclubByMonthRVAdapter(object : BookclubByMonthRVAdapter.MyItemClickListener {
            override fun onItemClick(participation: ArrayList<BookclubByMonth>) {
                val intent = Intent(context, BookclubJoin::class.java)
                startActivity(intent)
            }
        })

        val dummyBookclubByMonth = listOf(
            BookclubByMonth("", R.drawable.img_bookclub_book_1),
            BookclubByMonth("", R.drawable.img_bookclub_book_2),
            BookclubByMonth("", R.drawable.img_bookclub_book_3)
        )

        bookclubByMonthRVAdapter.setBookclubByMonth(dummyBookclubByMonth)
        binding.bookclubBookHomeMonthRv.adapter = bookclubByMonthRVAdapter

    }
}