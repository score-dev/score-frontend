package com.example.score_frontend

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import javax.net.ssl.SSLSocketFactory


object ApiClient {
    const val BASE_URL =
        "http://172.30.1.98:8080/"

    //HTTP 통신시 통신 정보를 인터셉트하여 로그로 출력
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                .setLevel(HttpLoggingInterceptor.Level.BODY)
                .setLevel(HttpLoggingInterceptor.Level.HEADERS)
        )
        .build()

    //리트로핏 정의
    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    //리트로핏 객체 생성
    fun <T> create(service: Class<T>): T {
        return getRetrofit().create(service)
    }

    fun tls(){
        val host = "example.com"
        val port = 443 // TLS 포트

        try {
            // SSL 소켓 팩토리를 생성하여 TLS 연결을 설정합니다.
            val sslSocketFactory = SSLSocketFactory.getDefault()
            val socket = sslSocketFactory.createSocket(host, port) as Socket

            // 입력 및 출력 스트림을 설정합니다.
            val inputStream: InputStream = socket.getInputStream()
            val outputStream: OutputStream = socket.getOutputStream()

            // 데이터를 전송하고 받는 코드를 여기에 작성합니다.
            // 예를 들어, outputStream를 사용하여 데이터를 서버로 전송하고
            // inputStream를 사용하여 서버에서 데이터를 읽을 수 있습니다.

            // 연결을 닫을 때, 소켓을 닫아 리소스를 정리합니다.
            socket.close()
        } catch (e: IOException) {
            // TLS 연결 오류 처리
            e.printStackTrace()
        }
    }
}