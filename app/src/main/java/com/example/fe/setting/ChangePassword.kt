package com.example.fe.setting

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.fe.R
import com.example.fe.databinding.ActivityChangePasswordBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword: AppCompatActivity() {
    private lateinit var binding: ActivityChangePasswordBinding
    private lateinit var newPasswordEditText: EditText
    private lateinit var passwordRecheckEditText: EditText
    private lateinit var passwordCountCheckImageView: ImageView
    private lateinit var passwordCaseCheckImageView: ImageView
    private lateinit var passwordMatchCheckImageView: ImageView
    private lateinit var passwordCountTextView: TextView
    private lateinit var passwordCaseTextView: TextView
    private lateinit var passwordMatchTextView: TextView
    private lateinit var togglePasswordVisibility1: ImageView
    private lateinit var togglePasswordVisibility2: ImageView
    private var isPasswordVisible: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        newPasswordEditText = binding.newPasswordEt
        passwordRecheckEditText = binding.passwordRecheckEt

        passwordCountCheckImageView = binding.passwordCountCheck
        passwordCaseCheckImageView = binding.passwordCaseCheck
        passwordMatchCheckImageView = binding.passwordMatchCheck

        passwordCountTextView = binding.passwordCountTv
        passwordCaseTextView = binding.passwordCaseTv
        passwordMatchTextView = binding.passwordMatchTv

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                checkPasswordConditions(newPasswordEditText.text.toString())
                checkPasswordMatch(newPasswordEditText.text.toString(), passwordRecheckEditText.text.toString())
                checkAllConditions()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        newPasswordEditText.addTextChangedListener(textWatcher)
        passwordRecheckEditText.addTextChangedListener(textWatcher)

        binding.changePasswordBtn.setOnClickListener {
            changePassword(newPasswordEditText.text.toString())
        }

        //비밀번호 보기
        togglePasswordVisibility1 = binding.togglePasswordVisibility1
        togglePasswordVisibility2 = binding.togglePasswordVisibility2

        newPasswordEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        passwordRecheckEditText.inputType = android.text.InputType.TYPE_CLASS_TEXT or android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        togglePasswordVisibility1.setOnClickListener {
            togglePasswordVisibility1()
        }

        togglePasswordVisibility2.setOnClickListener {
            togglePasswordVisibility2()
        }

    }

    private fun checkPasswordConditions(password: String) {
        if (password.length in 6..16) {
            passwordCountCheckImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryNormal))
            passwordCountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryNormal))
        } else {
            passwordCountCheckImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLabelAlternative))
            passwordCountTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLabelAlternative))
        }

        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasDigit = password.any { it.isDigit() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        if (hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar) {
            passwordCaseCheckImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryNormal))
            passwordCaseTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryNormal))
        } else {
            passwordCaseCheckImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLabelAlternative))
            passwordCaseTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLabelAlternative))
        }
    }

    private fun checkPasswordMatch(password: String, recheckPassword: String) {
        if (password == recheckPassword && password.isNotEmpty()) {
            passwordMatchCheckImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryNormal))
            passwordMatchTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryNormal))
        } else {
            passwordMatchCheckImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorLabelAlternative))
            passwordMatchTextView.setTextColor(ContextCompat.getColor(this, R.color.colorLabelAlternative))
        }
    }

    private fun checkAllConditions() {
        val isPasswordLengthValid = newPasswordEditText.text.length in 6..16
        val hasUpperCase = newPasswordEditText.text.any { it.isUpperCase() }
        val hasLowerCase = newPasswordEditText.text.any { it.isLowerCase() }
        val hasDigit = newPasswordEditText.text.any { it.isDigit() }
        val hasSpecialChar = newPasswordEditText.text.any { !it.isLetterOrDigit() }
        val isPasswordMatch = newPasswordEditText.text.toString() == passwordRecheckEditText.text.toString()

        binding.changePasswordBtn.isEnabled = isPasswordLengthValid && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar && isPasswordMatch
    }

    private fun changePassword(newPassword: String) {
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), "\"$newPassword\"")
        api.changePassword(requestBody).enqueue(object : Callback<ChangePasswordResponse> {
            override fun onResponse(call: Call<ChangePasswordResponse>, response: Response<ChangePasswordResponse>) {
                if (response.isSuccessful) {
                    val changePasswordResponse = response.body()
                    changePasswordResponse?.let {
                        if (it.isSuccess) {
                            Toast.makeText(this@ChangePassword, "비밀번호 변경 성공!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@ChangePassword, "비밀번호 변경 실패: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@ChangePassword, "비밀번호 변경 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ChangePasswordResponse>, t: Throwable) {
                Toast.makeText(this@ChangePassword, "네트워크 오류: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun togglePasswordVisibility1() {
        if (isPasswordVisible) {
            newPasswordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            togglePasswordVisibility1.setImageResource(R.drawable.ic_eye_off)
        } else {
            newPasswordEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            togglePasswordVisibility1.setImageResource(R.drawable.ic_eye)
        }
        isPasswordVisible = !isPasswordVisible
        newPasswordEditText.setSelection(newPasswordEditText.text.length) // 커서를 끝으로 이동
    }

    private fun togglePasswordVisibility2() {
        if (isPasswordVisible) {
            passwordRecheckEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            togglePasswordVisibility2.setImageResource(R.drawable.ic_eye_off)
        } else {
            passwordRecheckEditText.transformationMethod = HideReturnsTransformationMethod.getInstance()
            togglePasswordVisibility2.setImageResource(R.drawable.ic_eye)
        }
        isPasswordVisible = !isPasswordVisible
        passwordRecheckEditText.setSelection(passwordRecheckEditText.text.length) // 커서를 끝으로 이동
    }

}