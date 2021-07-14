package com.sunnyweather.android.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/*仓库层，在ViewModel和Model之间*/
object Repository {

    /*搜索城市数据，为了将异步获取的数据以响应式编程的方式通知给上一层，通常会返回一个LiveData对象，这里使用lifecycle-livedata-ktx库提供的功能，其自动构建并返回一个LiveData对象*/
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) { // Android不允许在主线程中进行网络请求，因此需要指定liveData()函数的线程类型为Dispatchers.IO
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query) // 调用了挂起函数
            if (placeResponse.status == "ok") {
                Log.d("hhh", "searchPlaces: ${placeResponse.places.toString()}")
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is ${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result) // 发送结果
    }
}