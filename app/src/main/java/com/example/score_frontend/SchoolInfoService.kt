package com.example.score_frontend

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SchoolInfoService {

    @GET("/hub/schoolInfo")
    fun getSchoolInfo(
        @Query("KEY") apiKey: String,
        @Query("Type") type: String,
        @Query("pIndex") pIndex: Int,
        @Query("pSize") pSize: Int,
        @Query("SCHUL_NM") schoolName: String
    ): Call<SchoolInfoResponse>

}