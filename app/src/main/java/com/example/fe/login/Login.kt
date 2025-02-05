package com.example.fe.login

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.*
import android.text.method.PasswordTransformationMethod
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.R
import com.example.fe.signup.TermsofUse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.identity.SignInCredential
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks

class Login : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var googleLoginButton: ImageButton
    private lateinit var signUpButton: TextView
    private lateinit var autoLoginCheckbox: CheckBox
    private lateinit var sharedPreferences: android.content.SharedPreferences
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailInput = findViewById(R.id.id_input)
        passwordInput = findViewById(R.id.pw_input)
        loginButton = findViewById(R.id.login_button)
        googleLoginButton = findViewById(R.id.google_login_button)
        autoLoginCheckbox = findViewById(R.id.login_checkbox)
        signUpButton = findViewById(R.id.signupButton)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        autoLoginCheckbox.isChecked = sharedPreferences.getBoolean("autoLogin", false)

        googleLoginButton.setOnClickListener {
            startGoogleLogin()
        }

        passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
        val pwShow = findViewById<ImageView>(R.id.pw_show)

        pwShow.setOnClickListener {
            if (passwordInput.transformationMethod == PasswordTransformationMethod.getInstance()) {
                passwordInput.transformationMethod = android.text.method.HideReturnsTransformationMethod.getInstance()
            } else {
                passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            passwordInput.setSelection(passwordInput.text.length)
        }

        oneTapClient = Identity.getSignInClient(this)
    }

    private fun startGoogleLogin() {
        val signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId("YOUR_WEB_CLIENT_ID") // Web Client ID 입력
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .build()

        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    startIntentSenderForResult(
                        result.pendingIntent.intentSender,
                        RC_GOOGLE_SIGN_IN,
                        null, 0, 0, 0
                    )
                } catch (e: Exception) {
                    Log.e("GOOGLE_LOGIN", "Intent 실행 오류: ${e.message}")
                }
            }
            .addOnFailureListener { e ->
                Log.e("GOOGLE_LOGIN", "Google 로그인 실패: ${e.message}")
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            try {
                val credential = oneTapClient.getSignInCredentialFromIntent(data)
                val idToken = credential.googleIdToken

                if (idToken != null) {
                    Log.d("GOOGLE_LOGIN", "받은 ID Token: $idToken")
                } else {
                    Log.e("GOOGLE_LOGIN", "ID Token이 없음")
                }
            } catch (e: ApiException) {
                Log.e("GOOGLE_LOGIN", "Google 로그인 오류: ${e.statusCode}")
            }
        }
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 9001
    }
}
