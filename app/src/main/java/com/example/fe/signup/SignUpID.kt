package com.example.fe.signup
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.fe.R

class SignUpID : AppCompatActivity() {


    private lateinit var emailInput: EditText
    private lateinit var emailError: TextView
    private lateinit var accountExistsError: TextView
    private lateinit var sendButton: Button
    private lateinit var verificationCodeInput: EditText
    private lateinit var emailLabel2: TextView
    private lateinit var timerText: TextView
    private lateinit var verificationCodeError: TextView
    private lateinit var nextButton: ImageButton

    private var isEmailValid = false
    private var isEmailExist = false
    private var verificationCodeSent = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_id)

        Log.d("SignUpID", "onCreate 실행됨")

        val backButton: ImageButton? = findViewById(R.id.back_button)
        backButton?.setOnClickListener {
            Log.d("SignUpID", "뒤로 가기 버튼 클릭됨 -> finish() 실행")
            finish()
        }

        // Initialize views
        emailInput = findViewById(R.id.pw_check_input)
        emailError = findViewById(R.id.email_error)
        accountExistsError = findViewById(R.id.account_exists_error)
        sendButton = findViewById(R.id.send_button)
        verificationCodeInput = findViewById(R.id.verification_code_input)
        emailLabel2 = findViewById(R.id.email_label2)
        timerText = findViewById(R.id.timer_text)
        verificationCodeError = findViewById(R.id.verification_code_error)
        nextButton = findViewById(R.id.next_button)

        // Hide elements initially
        emailError.visibility = View.GONE
        accountExistsError.visibility = View.GONE
        emailLabel2.visibility = View.GONE
        verificationCodeInput.visibility = View.GONE
        timerText.visibility = View.GONE
        verificationCodeError.visibility = View.GONE


        // Email validation logic
        emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val email = charSequence.toString()
                isEmailValid = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()

                if (!isEmailValid) {
                    emailError.visibility = View.VISIBLE
                    emailError.text = "이메일 형식이 올바르지 않습니다."
                } else {
                    emailError.visibility = View.GONE
                }

                // Check if email already exists (for demonstration, use a simple condition)
                isEmailExist = email == "already@exists.com" // Replace with actual check
                if (isEmailExist) {
                    accountExistsError.visibility = View.VISIBLE
                    accountExistsError.text = "이미 가입된 계정이 있습니다."
                } else {
                    accountExistsError.visibility = View.GONE
                }

                // Update send button state based on email validity
                sendButton.isEnabled = isEmailValid && !isEmailExist
                if (sendButton.isEnabled) {
                    sendButton.setBackgroundColor(resources.getColor(R.color.primary_50)) // Set to primary_40 color
                } else {
                    sendButton.setBackgroundColor(resources.getColor(R.color.primary_30)) // Set to default color
                }
            }

            override fun afterTextChanged(editable: Editable?) {}
        })

        // Send button click logic
        sendButton.setOnClickListener {
            if (isEmailValid && !isEmailExist) {
                // Show verification code inputs and timer
                emailLabel2.visibility = View.VISIBLE
                verificationCodeInput.visibility = View.VISIBLE
                timerText.visibility = View.VISIBLE
                startTimer()

                // Simulate sending the verification code (for demonstration purposes)
                verificationCodeSent = true
            }
        }

        // Verification code validation logic
        verificationCodeInput.addTextChangedListener {
            if (verificationCodeSent && it.toString() != "1234") { // Assuming 1234 is the correct code for demo
                verificationCodeError.visibility = View.VISIBLE
                verificationCodeError.text = "인증번호가 틀렸습니다. 입력한 정보를 확인해주세요."
            } else {
                verificationCodeError.visibility = View.GONE
            }
        }

        val nextButton: ImageButton = findViewById(R.id.next_button)
        nextButton.setOnClickListener {
            // 이메일 값 가져오기
            val email = emailInput.text.toString()
            // NextActivity로 이동
            val intent = Intent(this, SignUpPW::class.java)
            intent.putExtra("email", email) // 이메일 값 전달
            startActivity(intent)
        }

    }

    private fun startTimer() {
        object : CountDownTimer(3 * 60 * 1000, 1000) { // 3 minutes countdown
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timerText.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timerText.text = "00:00"
                // Optionally disable input or show message when time is over
            }
        }.start()
    }
}