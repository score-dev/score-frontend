package com.example.score_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.score_frontend.databinding.ActivityLogin1Binding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

class Login1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin1Binding
    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)

            // 이름, 이메일 등이 필요하다면 아래와 같이 account를 통해 각 메소드를 불러올 수 있다.
            val userName = account.givenName
            val serverAuth = account.serverAuthCode
            val id = account.id
            val email = account.email
            val accessToken = account.idToken
            Log.d("username", userName.toString())
            Log.d("id", id.toString())
            Log.d("accessToken", accessToken.toString())

            moveSignUpActivity()

        } catch (e: ApiException) {
            Log.e("this", e.stackTraceToString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartKakao.setOnClickListener{
            val intent= Intent( this,KakaoLoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnStartGoogle.setOnClickListener{
            addListener()
        }

        binding.btnStartNaver.setOnClickListener {
            val intent= Intent( this,NaverLoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addListener() {
        binding.btnStartGoogle.setOnClickListener {
            requestGoogleLogin()
        }
    }

    private fun requestGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(getString(R.string.default_web_client_id)) // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun moveSignUpActivity() {
        this.run {
            //  startActivity(Intent(this, SignUpActivity::class.java))
            // finish()
        }
    }

}