package com.example.fe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fe.databinding.FragmentBookclubBookBinding

class BookclubBookFragment : Fragment() {

    lateinit var binding: FragmentBookclubBookBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookclubBookBinding.inflate(inflater, container, false)

        return binding.root
    }
}
