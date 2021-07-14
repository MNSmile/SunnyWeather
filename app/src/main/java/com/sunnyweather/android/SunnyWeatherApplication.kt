package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object {
        const val TOKEN = "FWoO9PnvE10WEtvc"

        // 由于这里获取的不是Activity或Service中的context，而是Application中的Context，全局只存在一份实例，并且在整个应用程序的生命周期内都不会回收，因此不存在内存泄漏。
        @SuppressLint("StaticFieldLeak") // 忽略内存泄漏警告
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext // getApplicationContext()
    }
}