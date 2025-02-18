package com.example.fe.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityChangeInfoBinding
import com.example.fe.login.Login
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.fe.setting.api

class ChangeInfo:AppCompatActivity() {
    private lateinit var binding: ActivityChangeInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.passwordTv.setOnClickListener{
            val intent = Intent(this, VerifyPassword::class.java)
            startActivity(intent)
        }

        binding.alarmTv.setOnClickListener{
            val intent = Intent(this, Alarm::class.java)
            startActivity(intent)
        }

        binding.logoutTv.setOnClickListener {
            showLogoutConfirmationDialog()
        }

    }

    private fun showLogoutConfirmationDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.fragment_logout_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        val btnCancel = dialogView.findViewById<ImageView>(R.id.btn_logout_no)
        val btnConfirm = dialogView.findViewById<ImageView>(R.id.btn_logout_yes)

        btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        btnConfirm.setOnClickListener {
//            performLogout()
            alertDialog.dismiss()
        }
    }

//    private suspend fun performLogout() {
//        val accessToken = authToken
//        api.logout(accessToken).enqueue(object : Callback<LogoutResponse> {
//            override fun onResponse(call: Call<LogoutResponse>, response: Response<LogoutResponse>) {
//                if (response.isSuccessful) {
//                    val logoutResponse = response.body()
//                    logoutResponse?.let {
//                        if (it.isSuccess) {
//                            val intent = Intent(this@ChangeInfo, Login::class.java)
//                            startActivity(intent)
//                            finish()
//                        } else {
//                            Toast.makeText(this@ChangeInfo, "로그아웃 실패: ${it.message}", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                } else {
//                    Toast.makeText(this@ChangeInfo, "로그아웃 실패", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<LogoutResponse>, t: Throwable) {
//                Toast.makeText(this@ChangeInfo, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
}