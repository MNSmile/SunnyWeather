package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class Place(val name: String, val location: Location,
                 @SerializedName("formatted_address") val address: String) // 地区名字，经纬度，街道信息
