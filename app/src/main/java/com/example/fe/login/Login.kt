package com.example.fe.login

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.util.Log
import android.widget.*
import android.text.method.PasswordTransformationMethod
import android.text.style.UnderlineSpan
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.fe.MainActivity
import com.example.fe.R
import com.example.fe.databinding.ActivityLoginBinding
import com.example.fe.login.service.LoginRequest
import com.example.fe.login.service.LoginService
import com.example.fe.signup.TermsofUse
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.example.fe.login.service.LoginView
import com.example.fe.preference.Preference
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException


class Login : AppCompatActivity(), LoginView {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: ImageButton
    private lateinit var googleLoginButton: ImageButton
    private lateinit var signUpButton: TextView
    private lateinit var autoLoginCheckbox: CheckBox
    private lateinit var sharedPreferences: android.content.SharedPreferences
    private lateinit var oneTapClient: SignInClient
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d("LOGIN_TEST", account.idToken!!)
            sendIdTokenToBackend(account.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Google 로그인에 실패했습니다.", Toast.LENGTH_SHORT).show()
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.googleLoginButton.setOnClickListener {
            signIn()
        }

        binding.loginButton.setOnClickListener {
            login()
        }

        emailInput = findViewById(R.id.id_input)
        passwordInput = findViewById(R.id.pw_input)
        loginButton = findViewById(R.id.login_button)
        googleLoginButton = findViewById(R.id.google_login_button)
        autoLoginCheckbox = findViewById(R.id.login_checkbox_1)
        signUpButton = findViewById(R.id.signupButton)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        val savedAuthToken = sharedPreferences.getString("authToken", null)
        if (!savedAuthToken.isNullOrEmpty()) {
            Log.d("LOGIN_TEST", "저장된 accessToken 확인: $savedAuthToken")
        } else {
            Log.d("LOGIN_TEST", "저장된 accessToken이 없음")
        }

        autoLoginCheckbox.isChecked = sharedPreferences.getBoolean("autoLogin", false)

        val text = getString(R.string.signup_text)
        val spannable = SpannableString(text)
        spannable.setSpan(UnderlineSpan(), 0, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        signUpButton.text = spannable

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

        signUpButton.setOnClickListener {
            val intent = Intent(this@Login, TermsofUse::class.java)
            startActivity(intent)
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun sendIdTokenToBackend(idToken: String) {
        val requestBody = idToken.toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("http://3.38.209.11:8080/api/google-login")
            .post(requestBody)  //JSON 없이 Raw Body 전송
            .build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.e("GOOGLE_Login", "백엔드 요청 실패: ${e.message}")
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val responseBody = response.body?.string()
                Log.d("GOOGLE_Login", "백엔드 응답: $responseBody") // 응답 로그 추가
                // 구글 로그인 성공 후 Preference 화면으로 이동
                // JSON 응답에서 role 값 추출
                try {
                    val jsonResponse = JSONObject(responseBody)
                    val role = jsonResponse.getJSONObject("result").optString("role") // role 값 추출

                    // role에 따라 이동할 페이지 결정
                    runOnUiThread {
                        if (role == "GUEST") {
                            // role이 guest인 경우 Preference 페이지로 이동
                            startActivity(Intent(this@Login, Preference::class.java))
                        } else if (role == "USER") {
                            // role이 USER인 경우 Main 페이지로 이동
                            startActivity(Intent(this@Login, Preference::class.java))
                        }
                        finish() // 현재 액티비티 종료
                    }
                } catch (e: JSONException) {
                    Log.e("GOOGLE_Login", "JSON 파싱 오류: ${e.message}")
                }
            }
        })
    }


    private fun getUser(): LoginRequest {
        val email: String = binding.idInput.text.toString()
        val password: String = binding.pwInput.text.toString()

        Log.d("LOGIN_TEST", "입력된 이메일: $email, 비밀번호: $password")

        val loginRequest = LoginRequest(email, password)

        // JSON으로 변환 확인
        val jsonObject = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        Log.d("LOGIN_TEST", "전송할 JSON 데이터: $jsonObject")

        return loginRequest
    }



    private fun login() {
        val loginService = LoginService()
        loginService.setLoginView(this)
        loginService.login(getUser())
    }

    override fun saveAuthToken(token: String) {
        sharedPreferences.edit()
            .putString("authToken", token)
            .apply()

        Log.d("LOGIN_TEST", "일반 로그인 토큰 저장 완료: $token")

        // 로그인 성공 시 메인 화면으로 이동
        startActivity(Intent(this, Preference::class.java))
        finish()
    }

    override fun loginSuccess() {
        Toast.makeText(this, "로그인에 성공했습니다", Toast.LENGTH_SHORT).show()
    }

    override fun loginFailure(message: String) {
        Toast.makeText(this, "로그인에 실패했습니다: $message", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val RC_GOOGLE_SIGN_IN = 9001
    }
}
