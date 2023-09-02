package com.example.score_frontend

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        KakaoSdk.init(this, "9f1cd396bd1a8ab368b3cf4ddf2ab75f")
    }
}