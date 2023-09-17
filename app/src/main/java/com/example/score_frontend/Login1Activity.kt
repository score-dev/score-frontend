package com.example.score_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.score_frontend.databinding.ActivityLogin1Binding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin1Binding
    private lateinit var naverId: String
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

        binding.btnStartGoogle.setOnClickListener {
            requestGoogleLogin()
        }

        binding.btnStartNaver.setOnClickListener {
            /** Naver Login Module Initialize */
            val naverClientId = getString(R.string.social_login_info_naver_client_id)
            val naverClientSecret = getString(R.string.social_login_info_naver_client_secret)
            val naverClientName = getString(R.string.social_login_info_naver_client_name)
            NaverIdLoginSDK.initialize(this, "g5wBSflvhBgCmuxIpgkF", "g1VyPJPZjA" , "네이버아이디 로그인")

            // setLayoutState(false)

            binding.btnStartNaver.setOnClickListener {
                startNaverLogin()
            }

//        binding.tvNaverDeleteToken.setOnClickListener {
//            startNaverDeleteToken()
//        }
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

    /**
     * 로그인
     * authenticate() 메서드를 이용한 로그인 */
    private fun startNaverLogin(){
        var naverToken :String? = ""

        val profileCallback = object : NidProfileCallback<NidProfileResponse> {
            override fun onSuccess(response: NidProfileResponse) {
                val userId = response.profile?.id
                naverId = userId.toString()
                Log.d("id", userId.toString())
                Log.d("token", naverToken.toString())
                //     setLayoutState(true)
                Toast.makeText(this@Login1Activity, "네이버 아이디 로그인 성공!", Toast.LENGTH_SHORT).show()
                getNaverFun()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@Login1Activity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        /** OAuthLoginCallback을 authenticate() 메서드 호출 시 파라미터로 전달하거나 NidOAuthLoginButton 객체에 등록하면 인증이 종료되는 것을 확인할 수 있습니다. */
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                naverToken = NaverIdLoginSDK.getAccessToken()
//                var naverRefreshToken = NaverIdLoginSDK.getRefreshToken()
//                var naverExpiresAt = NaverIdLoginSDK.getExpiresAt().toString()
//                var naverTokenType = NaverIdLoginSDK.getTokenType()
//                var naverState = NaverIdLoginSDK.getState().toString()

                //로그인 유저 정보 가져오기
                NidOAuthLogin().callProfileApi(profileCallback)
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Toast.makeText(this@Login1Activity, "errorCode: ${errorCode}\n" +
                        "errorDescription: ${errorDescription}", Toast.LENGTH_SHORT).show()
            }
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }

    /**
     * 로그아웃
     * 애플리케이션에서 로그아웃할 때는 다음과 같이 NaverIdLoginSDK.logout() 메서드를 호출합니다. */
//    private fun startNaverLogout(){
//        NaverIdLoginSDK.logout()
//        setLayoutState(false)
//        Toast.makeText(this@NaverLoginActivity, "네이버 아이디 로그아웃 성공!", Toast.LENGTH_SHORT).show()
//    }

    /**
     * 연동해제
     * 네이버 아이디와 애플리케이션의 연동을 해제하는 기능은 다음과 같이 NidOAuthLogin().callDeleteTokenApi() 메서드로 구현합니다.
    연동을 해제하면 클라이언트에 저장된 토큰과 서버에 저장된 토큰이 모두 삭제됩니다.
     */
    private fun startNaverDeleteToken(){
        NidOAuthLogin().callDeleteTokenApi(this, object : OAuthLoginCallback {
            override fun onSuccess() {
                //서버에서 토큰 삭제에 성공한 상태입니다.
                // setLayoutState(false)
                Toast.makeText(this@Login1Activity, "네이버 아이디 토큰삭제 성공!", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(httpStatus: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                Log.d("naver", "errorCode: ${NaverIdLoginSDK.getLastErrorCode().code}")
                Log.d("naver", "errorDesc: ${NaverIdLoginSDK.getLastErrorDescription()}")
            }
            override fun onError(errorCode: Int, message: String) {
                // 서버에서 토큰 삭제에 실패했어도 클라이언트에 있는 토큰은 삭제되어 로그아웃된 상태입니다.
                // 클라이언트에 토큰 정보가 없기 때문에 추가로 처리할 수 있는 작업은 없습니다.
                onFailure(errorCode, message)
            }
        })
    }

//    private fun setLayoutState(login: Boolean){
//        if(login){
//            binding.tvNaverLogin.visibility = View.GONE
//            binding.tvNaverLogout.visibility = View.VISIBLE
//            binding.tvNaverDeleteToken.visibility = View.VISIBLE
//        }else{
//            binding.tvNaverLogin.visibility = View.VISIBLE
//            binding.tvNaverLogout.visibility = View.GONE
//            binding.tvNaverDeleteToken.visibility = View.GONE
//            binding.tvResult.text = ""
//        }
//    }

    private fun getNaverFun() {

        LoginService.retrofitPostLogin(naverId)
            .enqueue(object : Callback<String> { // 응답 타입을 String으로 지정
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    if (response.isSuccessful) {
                        val responseData = response.body()
                        println("네이버 로그인 성공: $responseData")
                    } else {
                        val errorBody = response.errorBody()?.string()
                        println("네이버 로그인 실패: $errorBody")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("TAG", "실패원인: $t")
                }
            })
    }

}