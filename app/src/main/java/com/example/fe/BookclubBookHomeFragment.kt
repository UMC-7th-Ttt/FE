package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fe.databinding.FragmentBookclubBookHomeBinding


class BookclubBookHomeFragment: Fragment() {
    lateinit var binding: FragmentBookclubBookHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookHomeBinding.inflate(inflater, container, false)

        initBookclubMemberRecyclerview()

        return binding.root
    }

    private fun initBookclubMemberRecyclerview(){
        binding.bookclubBookHomeMembersRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.bookclubBookHomeMonthRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bookclubMemberRVAdapter = BookclubMemberRVAdapter()
        val bookclubByMonthRVAdapter = BookclubByMonthRVAdapter()

        val dummyMembers = listOf(
            BookclubMember("주디",R.drawable.img_member1),
            BookclubMember("파도",R.drawable.img_member2),
            BookclubMember("민준",R.drawable.img_member3),
            BookclubMember("현규",R.drawable.img_member4)
        )

        val dummyBookclubByMonth = listOf(
            BookclubByMonth("1월"),
            BookclubByMonth("2월"),
            BookclubByMonth("3월"),
            BookclubByMonth("4월")
        )

        bookclubMemberRVAdapter.setMembers(dummyMembers)
        bookclubByMonthRVAdapter.setBookclubByMonth(dummyBookclubByMonth)

        binding.bookclubBookHomeMembersRv.adapter = bookclubMemberRVAdapter
        binding.bookclubBookHomeMonthRv.adapter = bookclubByMonthRVAdapter

    }


}