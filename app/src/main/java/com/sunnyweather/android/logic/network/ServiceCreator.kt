package com.sunnyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)                                  // 指定所有 Retrofit 请求的根路径
        .addConverterFactory(GsonConverterFactory.create()) // 指定 Retrofit 在解析数据是所用的转换库，这里指定为 GsonConverterFactory。
        .build()

    /*动态代理*/
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /*泛型实化*/
    inline fun <reified T> create(): T = create(T::class.java)
}