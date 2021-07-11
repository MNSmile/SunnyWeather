package com.sunnyweather.android.logic.network

import retrofit2.Call
import com.sunnyweather.android.SunnyWeatherApplication
import retrofit2.http.GET
import retrofit2.http.Query
import com.sunnyweather.android.logic.model.PlaceResponse

interface PlaceService {
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN") // GET注解配置相对地址
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse> // 注意这里是 import retrofit2.Call
}