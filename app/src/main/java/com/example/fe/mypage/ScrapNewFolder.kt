package com.example.fe.mypage

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fe.R
import com.example.fe.databinding.ActivityScrapNewFolderBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ScrapNewFolder : AppCompatActivity() {
    private lateinit var binding: ActivityScrapNewFolderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScrapNewFolderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        // EditText 배경 설정 함수
        fun setEditTextBackground(editText: EditText, isFocused: Boolean) {
            if (isFocused) {
                editText.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorPrimaryNormal)
            } else {
                editText.backgroundTintList = ContextCompat.getColorStateList(this, R.color.colorLabelAssistive)
            }
        }

        val folderNameEditText = binding.folderNameEt

        folderNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createBtn.isEnabled = folderNameEditText.text.isNotEmpty()
                setEditTextBackground(folderNameEditText, true)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        folderNameEditText.setOnClickListener {
            setEditTextBackground(folderNameEditText, true)
        }

        folderNameEditText.setOnFocusChangeListener { _, hasFocus ->
            setEditTextBackground(folderNameEditText, hasFocus)
        }

        binding.createBtn.setOnClickListener {
            val folderName = folderNameEditText.text.toString()
            if (folderName.isNotEmpty()) {
                createNewFolder(folderName)
            } else {
            }
        }
    }

    private fun createNewFolder(folderName: String) {
        // 폴더 생성 로직을 여기에 작성
        api.createFolder(folderName).enqueue(object : Callback<CreateFolderResponse> {
            override fun onResponse(call: Call<CreateFolderResponse>, response: Response<CreateFolderResponse>) {
                if (response.isSuccessful) {
                    val scrapFolderResponse = response.body()
                    scrapFolderResponse?.let {
                        val intent = Intent(this@ScrapNewFolder, MyPageScrapDetail::class.java)
                        intent.putExtra("folderId", it.result.folderId)
                        intent.putExtra("folderName", folderName)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.e("MyPageScrapFragment", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CreateFolderResponse>, t: Throwable) {
                Log.e("MyPageScrapFragment", "Network Error: ${t.message}")
            }
        })
    }
}