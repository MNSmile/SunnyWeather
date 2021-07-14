package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName



data class RealtimeResponce(val staus: String, val result: Result) {
    data class Result(val realtime: Realtime)
    data class Realtime(val skycon: String, val temperature: Float,
                        @SerializedName("air_quality") val airQuality: AirQuality)
    data class AirQuality(val api: AQI)
    data class AQI(val chn: Float)
}
