package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

data class PlaceResponse(val status: String, val places: List<Place>)

data class Place(val name: String, val location: Location,
                 @SerializedName("formatted_address") val address: String) // 地区名字，经纬度，街道信息

data class Location(val lng: String, val lat: String) // 经度纬度信息