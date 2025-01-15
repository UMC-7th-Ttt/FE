package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fe.databinding.FragmentBookclubBookParticipantBinding

class BookclubBookParticipationFragment: Fragment() {
    lateinit var binding: FragmentBookclubBookParticipantBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookParticipantBinding.inflate(inflater, container, false)

        initParticipationRecyclerview()

        return binding.root
    }

    private fun initParticipationRecyclerview(){
        binding.participantRv.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        val bookclubParticipationRVAdapter = BookclubParticipationRVAdapter()

        val dummyParticipation = listOf(
            BookclubParticipation("1"),
            BookclubParticipation("2"),
            BookclubParticipation("3"),
            BookclubParticipation("4")
        )

        bookclubParticipationRVAdapter.setParticipation(dummyParticipation)

        binding.participantRv.adapter = bookclubParticipationRVAdapter

    }
}