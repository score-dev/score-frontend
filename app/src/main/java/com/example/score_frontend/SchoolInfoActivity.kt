package com.example.score_frontend

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.score_frontend.databinding.ActivityOnboardingNicknameBinding
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SchoolInfoActivity: AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingNicknameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingNicknameBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        main()
    }

    fun main() {
        val apiKey = "de6bbfe0ce264cdf95325bf8019bf718" // 실제 API 키를 여기에 입력
        val schoolName = "목동" // 학교 이름 또는 일부를 여기에 입력

        val retrofit = Retrofit.Builder()
            .baseUrl("https://open.neis.go.kr")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(SchoolInfoService::class.java)

        val call = service.getSchoolInfo(apiKey, "json", 1, 100, schoolName)

        call.enqueue(object : retrofit2.Callback<SchoolInfoResponse> {
            override fun onResponse(call: Call<SchoolInfoResponse>, response: retrofit2.Response<SchoolInfoResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        // "schoolInfo" 배열 안에 있는 모든 학교의 "SCHUL_NM"을 추출
                        val schoolNames = it.schoolInfo.flatMap { schoolInfo ->
                            (schoolInfo["row"] as? List<Map<String, String>>)?.mapNotNull { it["SCHUL_NM"] }
                                ?: emptyList()
                        }
                        println("학교 이름 목록: $schoolNames")
                    }
                } else {
                    println("요청 실패. 상태 코드: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SchoolInfoResponse>, t: Throwable) {
                println("요청 실패. 오류 메시지: ${t.message}")
            }
        })
    }
}