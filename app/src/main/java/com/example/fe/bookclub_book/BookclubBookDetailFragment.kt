package com.example.fe.bookclub_book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.R
import com.example.fe.bookclub_book.adapter.BookclubBookDetailMemberRVAdapter
import com.example.fe.bookclub_book.dataclass.BookclubDetailMember
import com.example.fe.bookclub_book.dataclass.BookclubMember
import com.example.fe.databinding.FragmentBookclubBookDetailBinding

class BookclubBookDetailFragment : Fragment() {

    private lateinit var binding: FragmentBookclubBookDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookDetailBinding.inflate(inflater, container, false)

        initBookclubDetailMemberRecyclerview()

        return binding.root
    }

    private fun initBookclubDetailMemberRecyclerview() {
        binding.bookclubDetailMemberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bookclubDetailMemberRVAdapter = BookclubBookDetailMemberRVAdapter()

        val dummyDetailMembers = listOf(
            BookclubDetailMember("주디", R.drawable.img_bookclub_detail_member1),
            BookclubDetailMember("파도", R.drawable.img_bookclub_detail_member2),
            BookclubDetailMember("민준", R.drawable.img_bookclub_detail_member3),
            BookclubDetailMember("현규", R.drawable.img_bookclub_detail_member4),
            BookclubDetailMember("주디", R.drawable.img_bookclub_detail_member5),
            BookclubDetailMember("파도", R.drawable.img_bookclub_detail_member6),
            BookclubDetailMember("민준", R.drawable.img_bookclub_detail_member7),
            BookclubDetailMember("현규", R.drawable.img_bookclub_detail_member8)
        )

        bookclubDetailMemberRVAdapter.setMembers(dummyDetailMembers)

        binding.bookclubDetailMemberRv.adapter = bookclubDetailMemberRVAdapter
    }
}
