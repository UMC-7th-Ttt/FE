package com.example.fe.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.text.method.PasswordTransformationMethod
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fe.R
import com.example.fe.signup.TermsofUse

class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val emailInput = findViewById<EditText>(R.id.id_input)
        val passwordInput = findViewById<EditText>(R.id.pw_input)
        val loginButton = findViewById<ImageButton>(R.id.login_button)
        val eyeIcon = findViewById<ImageView>(R.id.pw_show)
        val autoLoginCheckbox = findViewById<CheckBox>(R.id.login_checkbox)
        val findPw = findViewById<TextView>(R.id.find_pw)
        val signupLink = findViewById<TextView>(R.id.textView3)

        // SharedPreferences for Auto-Login
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        autoLoginCheckbox.isChecked = sharedPreferences.getBoolean("autoLogin", false)

        // Add text watchers for email and password input
        emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs(emailInput, passwordInput, loginButton)
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                validateInputs(emailInput, passwordInput, loginButton)
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        // Eye icon click to show/hide password
        eyeIcon.setOnClickListener {
            if (passwordInput.transformationMethod is PasswordTransformationMethod) {
                passwordInput.transformationMethod = null // Show password
                eyeIcon.setImageResource(R.drawable.ic_login_eye_open) // Change to open eye icon
            } else {
                passwordInput.transformationMethod = PasswordTransformationMethod() // Hide password
                eyeIcon.setImageResource(R.drawable.ic_signup_pw_eye) // Change to closed eye icon
            }
            passwordInput.setSelection(passwordInput.text.length) // Keep the cursor at the end
        }

        // Checkbox for Auto-Login
        autoLoginCheckbox.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPreferences.edit()
            editor.putBoolean("autoLogin", isChecked)
            editor.apply()
        }

        // Navigate to Password Reset Page
        findPw.setOnClickListener {
            val intent = Intent(this@Login, PasswordResetActivity::class.java)
            startActivity(intent)
        }

        // Perform login on button click
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()
            if (isValidLogin(email, password)) {
                // Proceed with login
                Toast.makeText(this@Login, "Login successful", Toast.LENGTH_SHORT).show()
            } else {
                // Show error message
                Toast.makeText(this@Login, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to Signup Page
        signupLink.setOnClickListener {
            val intent = Intent(this@Login, TermsofUse::class.java)
            startActivity(intent)
        }
    }

    // Validate inputs and enable login button
    private fun validateInputs(emailInput: EditText, passwordInput: EditText, loginButton: ImageButton) {
        val email = emailInput.text.toString()
        val password = passwordInput.text.toString()
        loginButton.isEnabled = isValidEmail(email) && isValidPassword(password)
    }

    // Check if email is valid
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Check if password is valid
    private fun isValidPassword(password: String): Boolean {
        return password.length in 6..16
    }

    // Validate login credentials
    private fun isValidLogin(email: String, password: String): Boolean {
        // Replace this with real login validation logic
        return email == "Ttt@naver.com" && password == "password123"
    }
}
