package com.example.fe.setting

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityVerifyPasswordBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifyPassword: AppCompatActivity() {
    private lateinit var binding: ActivityVerifyPasswordBinding
    private lateinit var passwordEditText: EditText
    private lateinit var togglePasswordVisibility: ImageView
    private var isPasswordVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerifyPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        passwordEditText = findViewById(R.id.password_et)
        togglePasswordVisibility = findViewById(R.id.toggle_password_visibility)

        passwordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        togglePasswordVisibility.setOnClickListener {
            togglePasswordVisibility()
        }

        fun checkEditTexts() {
            val allFilled = passwordEditText.text.isNotEmpty()
            binding.nextBtn.isEnabled = allFilled // 버튼 활성화/비활성화
        }

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkEditTexts() // 텍스트가 변경될 때마다 호출
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.nextBtn.setOnClickListener{
            verifyPassword(passwordEditText.text.toString())
        }

    }

    private fun verifyPassword(password: String) {
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), """{"password": "$password"}""")
        api.verifyPassword(requestBody).enqueue(object : Callback<VerifyPasswordResponse> {
            override fun onResponse(call: Call<VerifyPasswordResponse>, response: Response<VerifyPasswordResponse>) {
                if (response.isSuccessful) {
                    val certificationResponse = response.body()
                    certificationResponse?.let {
                        if (it.isSuccess) {
                            val intent = Intent(this@VerifyPassword, ChangePassword::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@VerifyPassword, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@VerifyPassword, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<VerifyPasswordResponse>, t: Throwable) {
                Toast.makeText(this@VerifyPassword, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            togglePasswordVisibility.setImageResource(R.drawable.ic_eye_off)
        } else {
            passwordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            togglePasswordVisibility.setImageResource(R.drawable.ic_eye)
        }
        isPasswordVisible = !isPasswordVisible
        passwordEditText.setSelection(passwordEditText.text.length) // 커서를 끝으로 이동
    }
}
