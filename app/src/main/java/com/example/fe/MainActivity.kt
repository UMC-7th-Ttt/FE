package com.example.fe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.databinding.ActivityMainBinding
import com.example.fe.mypage.MyPageFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mypageTv.setOnClickListener{
            val mypageFragment = MyPageFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, mypageFragment) // fragment_container는 Fragment를 담는 ViewGroup의 ID
                .addToBackStack(null) // 백스택에 추가하여 뒤로 가기 가능
                .commit()
        }
    }


}