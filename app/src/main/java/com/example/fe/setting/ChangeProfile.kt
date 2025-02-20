package com.example.fe.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.fe.JohnRetrofitClient
import com.example.fe.R
import com.example.fe.databinding.ActivityChangeProfileBinding
import com.example.fe.mypage.server.UserResponse
import com.example.fe.mypage.server.MyPageRetrofitInterface
import com.example.fe.setting.server.ChangeProfileResponse
import com.example.fe.setting.server.SettingRetrofitInterface
import com.example.fe.signup.service.NicknameService
import com.example.fe.signup.service.NicknameView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class ChangeProfile : AppCompatActivity(), NicknameView {
    private lateinit var binding: ActivityChangeProfileBinding
    private lateinit var authService: NicknameService  // 서비스 선언
    private var profilePictureFile: File? = null

    @SuppressLint("IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authService = NicknameService()  // 초기화
        authService.setNicknameView(this)  // View 연결

        binding.nicknameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.checkDuplicateBtn.isEnabled = s?.isNotEmpty() == true
                binding.nicknamePossTv.visibility = View.GONE
                binding.nicknameErrorTv.visibility = View.GONE
                binding.changeBtn.isEnabled = false
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.checkDuplicateBtn.setOnClickListener {
            val nickname = binding.nicknameEt.text.toString()
            authService.nickName(nickname)  // 닉네임 중복 확인 요청
        }

        binding.backBtn.setOnClickListener {
            finish()
        }

        // 사진 넣기 기능 (갤러리에서 선택)
        binding.changeImageBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        binding.changeBtn.setOnClickListener {
            updateProfile()
        }

        setupUI()
        getUser()
    }

    private fun setupUI() {
        binding.nicknamePossTv.visibility = View.GONE
        binding.nicknameErrorTv.visibility = View.GONE
        binding.changeBtn.isEnabled = false
        binding.checkDuplicateBtn.isEnabled = false
    }

    // 갤러리에서 선택한 이미지 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data
            profilePictureFile = selectedImageUri?.let { uri -> getFileFromUri(uri) }
            binding.profileImageIv.setImageURI(selectedImageUri)  // 선택한 이미지 설정
        }
    }

    private fun getFileFromUri(uri: Uri): File {
        val inputStream: InputStream? = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "profile_picture.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file
    }

    private fun updateProfile() {
        val nickname = binding.nicknameEt.text.toString()

        val requestDTO = RequestBody.create("application/json".toMediaTypeOrNull(), """{"nickname": "$nickname"}""")
        val profilePicturePart = profilePictureFile?.let {
            val requestFile = it.asRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("profilePicture", it.name, requestFile)
        }

        val api = JohnRetrofitClient.getClient(this).create(SettingRetrofitInterface::class.java)
        api.changeProfile(requestDTO, profilePicturePart).enqueue(object : Callback<ChangeProfileResponse> {
            override fun onResponse(call: Call<ChangeProfileResponse>, response: Response<ChangeProfileResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        if (it.isSuccess) {
                            Toast.makeText(this@ChangeProfile, "프로필이 업데이트되었습니다.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ChangeProfile, "프로필 업데이트 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@ChangeProfile, "프로필 업데이트 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChangeProfileResponse>, t: Throwable) {
                Toast.makeText(this@ChangeProfile, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getUser() {
        val api = JohnRetrofitClient.getClient(this).create(MyPageRetrofitInterface::class.java)
        api.getUser().enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val getUserResponse = response.body()
                    getUserResponse?.let {
                        Glide.with(this@ChangeProfile)
                            .load(it.result.profileUrl)
                            .into(binding.profileImageIv)
                    }
                } else {
                    Log.e("MyPageScrapFragment", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.e("MyPageScrapFragment", "Network Error: ${t.message}")
            }
        })
    }

    override fun nicknameCheckSuccess() {
        binding.nicknamePossTv.visibility = View.VISIBLE
        binding.nicknameErrorTv.visibility = View.GONE
        binding.nicknameEt.background = ContextCompat.getDrawable(this, R.drawable.primary_border_radius_5)
        binding.changeBtn.isEnabled = true
    }

    override fun nicknameCheckFailure() {
        binding.nicknamePossTv.visibility = View.GONE
        binding.nicknameErrorTv.visibility = View.VISIBLE
        binding.nicknameEt.background = ContextCompat.getDrawable(this, R.drawable.red_border_radius_5)
        binding.changeBtn.isEnabled = false
    }
}