package com.example.fe

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.regex.Pattern

class SignUpPW : AppCompatActivity() {

    private lateinit var pwInput: EditText
    private lateinit var pwCheckInput: EditText
    private lateinit var pwCheck1Tv: TextView
    private lateinit var pwCheck2Tv: TextView
    private lateinit var pwCheckCheckTv: TextView
    private lateinit var pwCheck1: ImageView
    private lateinit var pwCheck2: ImageView
    private lateinit var pwCheckCheck: ImageView
    private lateinit var pwShow: ImageView
    private lateinit var pwCheckShow: ImageView
    private lateinit var nextButton: ImageButton
    private var isPasswordVisible = false
    private var isPasswordConfirmed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_pw)

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            finish() // 현재 액티비티 종료 후 이전 페이지로 돌아감
        }

        // Initialize views
        pwInput = findViewById(R.id.pw_input)
        pwCheckInput = findViewById(R.id.pw_check_input)
        pwCheck1Tv = findViewById(R.id.pw_check1_tv)
        pwCheck2Tv = findViewById(R.id.pw_check2_tv)
        pwCheckCheckTv = findViewById(R.id.pw_check_check_tv)
        pwCheck1 = findViewById(R.id.pw_check1)
        pwCheck2 = findViewById(R.id.pw_check2)
        pwCheckCheck = findViewById(R.id.pw_check_check)
        pwShow = findViewById(R.id.pw_show)
        pwCheckShow = findViewById(R.id.pw_check_show)
        nextButton = findViewById(R.id.next_button)

        // Set initial password visibility state
        pwInput.inputType = 129  // Password input type (hidden)
        pwCheckInput.inputType = 129  // Password input type (hidden)

        // Add text change listeners for password and confirm password fields
        pwInput.addTextChangedListener(passwordTextWatcher)
        pwCheckInput.addTextChangedListener(passwordTextWatcher)

        // Set password visibility toggle on pwShow and pwCheckShow click
        pwShow.setOnClickListener { togglePasswordVisibility(pwInput) }
        pwCheckShow.setOnClickListener { togglePasswordVisibility(pwCheckInput) }

        // Initially disable the confirm password field
        pwCheckInput.isEnabled = false
        // Initialize next button state
        nextButton.isEnabled = false

        // Enable pwCheckInput when pwInput has text
        pwInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                if (editable.isNullOrEmpty()) {
                    pwCheckInput.isEnabled = false  // Disable confirm password field if pwInput is empty
                } else {
                    pwCheckInput.isEnabled = true  // Enable confirm password field when pwInput is filled
                }
                updateNextButtonState()  // Update the next button state
            }
        })

        // Listener for confirm password field to check if passwords match
        pwCheckInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                checkPasswordMatch()  // Check if passwords match
            }
        })

        // Apply edge-to-edge for the root layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun togglePasswordVisibility(editText: EditText) {
        if (isPasswordVisible) {
            editText.inputType = 129 // Hide password (password type)
        } else {
            editText.inputType = 1 // Show password (text type)
        }
        isPasswordVisible = !isPasswordVisible
        editText.setSelection(editText.text.length) // Maintain cursor position
    }

    private fun checkPasswordMatch() {
        val password = pwInput.text.toString()
        val confirmPassword = pwCheckInput.text.toString()

        if (password == confirmPassword) {
            isPasswordConfirmed = true
            pwCheckCheckTv.setTextColor(ContextCompat.getColor(this, R.color.primary_50))  // Change text color
            pwCheckCheck.setColorFilter(ContextCompat.getColor(this, R.color.primary_50)) // Change icon color
        } else {
            isPasswordConfirmed = false
            pwCheckCheckTv.setTextColor(ContextCompat.getColor(this, R.color.white_20))  // Reset text color
            pwCheckCheck.setColorFilter(ContextCompat.getColor(this, R.color.white_20)) // Reset icon color
        }
        updateNextButtonState()
    }

    private fun updateNextButtonState() {
        // Check all conditions (password length, pattern, and match)
        val isPasswordValid = isPasswordLengthValid() && isPasswordPatternValid()
        nextButton.isEnabled = isPasswordValid && isPasswordConfirmed
    }

    private fun isPasswordLengthValid(): Boolean {
        val password = pwInput.text.toString()
        if (password.length in 6..16) {
            pwCheck1Tv.setTextColor(ContextCompat.getColor(this, R.color.primary_50))  // Valid length text color
            pwCheck1.setColorFilter(ContextCompat.getColor(this, R.color.primary_50))  // Valid length icon color
            return true
        } else {
            pwCheck1Tv.setTextColor(ContextCompat.getColor(this, R.color.white_20))  // Reset text color
            pwCheck1.setColorFilter(ContextCompat.getColor(this, R.color.white_20))  // Reset icon color
            return false
        }
    }

    private fun isPasswordPatternValid(): Boolean {
        val password = pwInput.text.toString()
        // Regex for checking upper/lowercase, number, and special character
        val pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+={}|;:'\"<>,.?/]).{6,16}$")
        val isValid = pattern.matcher(password).matches()

        if (isValid) {
            pwCheck2Tv.setTextColor(ContextCompat.getColor(this, R.color.primary_50))  // Valid pattern text color
            pwCheck2.setColorFilter(ContextCompat.getColor(this, R.color.primary_50))  // Valid pattern icon color
        } else {
            pwCheck2Tv.setTextColor(ContextCompat.getColor(this, R.color.white_20))  // Reset text color
            pwCheck2.setColorFilter(ContextCompat.getColor(this, R.color.white_20))  // Reset icon color
        }

        return isValid
    }

    private val passwordTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, after: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            updateNextButtonState()  // Update next button state
        }
    }
}
