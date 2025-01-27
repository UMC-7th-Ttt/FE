package com.example.fe

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SignUpName : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up_name)

        val nicknameInput: EditText = findViewById(R.id.nickname_input)
        val checkButton: Button = findViewById(R.id.send_button)
        val errorMessage: TextView = findViewById(R.id.error_message)
        val pic2Button: ImageButton = findViewById(R.id.pic2)
        val pic1ImageView: ImageView = findViewById(R.id.pic1)
        val backButton: ImageButton = findViewById(R.id.back_button)
        val nextButton: ImageButton = findViewById(R.id.next_button)

        // Handle duplicate nickname check
        checkButton.setOnClickListener {
            val nickname = nicknameInput.text.toString()
            if (nickname == "1234") {  // Replace with your actual check (e.g., API call) usedNickname
                errorMessage.visibility = View.VISIBLE
            } else {
                errorMessage.visibility = View.GONE
            }
        }

        // Handle selecting an image for pic1
        pic2Button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
            startActivityForResult(intent, 100)
        }

        // Handle back button click
        backButton.setOnClickListener {
            onBackPressed()  // or finish() to explicitly close the current activity
        }

        // Handle next button click to navigate to next page
        nextButton.setOnClickListener {
            val intent = Intent(this, SignUpComplete::class.java) // Replace NextActivity with your next activity
            startActivity(intent)
        }

        // For handling edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val selectedImageUri = data?.data
            findViewById<ImageView>(R.id.pic1).setImageURI(selectedImageUri)
        }
    }
}
