package com.example.fe.bookclub_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
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

        return binding.root
    }

    private fun initBookclubMemberRecyclerview() {
        binding.bookclubBookHomeMembersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.bookclubBookHomeMonthRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bookclubMemberRVAdapter = BookclubMemberRVAdapter(object : BookclubMemberRVAdapter.MyItemClickListener {
            override fun onItemClick(participation: ArrayList<BookclubMember>) {
                // Fragment 전환
                val detailFragment = BookclubBookReviewFragment()
                parentFragmentManager.commit {
                    replace(R.id.fragment_container, detailFragment)
                    addToBackStack(null)
                }
            }
        })

        val bookclubByMonthRVAdapter = BookclubByMonthRVAdapter(object : BookclubByMonthRVAdapter.MyItemClickListener {
            override fun onItemClick(participation: ArrayList<BookclubByMonth>) {
                // Fragment 전환
                val detailFragment = BookclubBookRegisterFragment()
                parentFragmentManager.commit {
                    replace(R.id.fragment_container, detailFragment)
                    addToBackStack(null)
                }
            }
        })

        val dummyMembers = listOf(
            BookclubMember("주디", R.drawable.img_bookclub_member),
            BookclubMember("파도", R.drawable.img_bookclub_member),
            BookclubMember("민준", R.drawable.img_bookclub_member),
            BookclubMember("현규", R.drawable.img_bookclub_member)
        )

        val dummyBookclubByMonth = listOf(
            BookclubByMonth("", R.drawable.img_bookclub_book_1),
            BookclubByMonth("", R.drawable.img_bookclub_book_2),
            BookclubByMonth("", R.drawable.img_bookclub_book_3)
        )

        bookclubMemberRVAdapter.setMembers(dummyMembers)
        bookclubByMonthRVAdapter.setBookclubByMonth(dummyBookclubByMonth)

        binding.bookclubBookHomeMembersRv.adapter = bookclubMemberRVAdapter
        binding.bookclubBookHomeMonthRv.adapter = bookclubByMonthRVAdapter

    }
}