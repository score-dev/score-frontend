package com.example.score_frontend

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

    @GET("/user/auth/naver")
    fun postLogin(
        @Query("id") id: String
    ): Call<String>

    companion object{

        fun retrofitPostLogin(id: String): Call<String> {
            return ApiClient.create(LoginService::class.java).postLogin(id)
        }

    }
}